package com.tokopedia.shop_widget.note.view.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.accordion.AccordionDataUnify
import com.tokopedia.accordion.AccordionUnify
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.globalerror.ReponseStatus
import com.tokopedia.kotlin.extensions.view.getScreenHeight
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.shop.common.graphql.data.shopnote.ShopNoteModel
import com.tokopedia.shop_widget.R
import com.tokopedia.shop_widget.databinding.FragmentShopNoteBottomSheetBinding
import com.tokopedia.shop_widget.note.di.component.DaggerShopNoteComponent
import com.tokopedia.shop_widget.note.di.module.ShopNoteModule
import com.tokopedia.shop_widget.note.view.viewmodel.ShopNoteBottomSheetViewModel
import com.tokopedia.shop_widget.utils.ShopWidgetExceptionHandler.logExceptionToCrashlytics
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.LoaderUnify
import com.tokopedia.unifycomponents.toDp
import com.tokopedia.unifycomponents.toPx
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.utils.lifecycle.autoClearedNullable
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

class ShopNoteBottomSheet : BottomSheetUnify() {

    companion object {
        val TAG = ShopNoteBottomSheet::class.java.simpleName
        private const val SHOP_ID = "EXTRA_SHOP_ID"

        @JvmStatic
        fun createInstance(shopId: String?) = ShopNoteBottomSheet().apply {
            arguments = Bundle().apply {
                putString(SHOP_ID, shopId)
            }
        }
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private var viewModel: ShopNoteBottomSheetViewModel? = null

    private var buyerShopId: String? = null
    private var loader: LoaderUnify? = null
    private var globalError: GlobalError? = null
    private var accordion: AccordionUnify? = null
    private var viewBinding by autoClearedNullable<FragmentShopNoteBottomSheetBinding>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initInjector()
        setupUi()

        viewModel = ViewModelProvider(this, viewModelFactory).get(ShopNoteBottomSheetViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupObserver()

        arguments?.run {
            buyerShopId = getString(SHOP_ID)
        }
        getShopNotes()
    }

    private fun setupUi() {
        viewBinding = FragmentShopNoteBottomSheetBinding.inflate(LayoutInflater.from(context))
        viewBinding?.let {
            setChild(it.root)
            loader = it.loader
            globalError = it.globalError
            accordion = it.accordion
            setTitle(getString(R.string.shop_note_title))
            setCloseClickListener {
                dismiss()
            }
        }

        showKnob = true
        showCloseIcon = !showKnob
        isDragable = showKnob
        isHideable = showKnob
        clearContentPadding = showKnob
        customPeekHeight = (getScreenHeight() / 2).toDp()
    }

    private fun initInjector() {
        activity?.run {
            DaggerShopNoteComponent
                    .builder()
                    .shopNoteModule(ShopNoteModule())
                    .baseAppComponent((requireContext().applicationContext as BaseMainApplication).baseAppComponent)
                    .build()
                    .inject(this@ShopNoteBottomSheet)
        }
    }

    private fun setupObserver() {
        viewModel?.shopNotes?.observe(viewLifecycleOwner) { result ->
            loader?.gone()
            accordion?.show()
            when(result) {
                is Success -> {
                    result.data.forEach { model ->
                        setAccordion(model)
                    }
                }
                is Fail -> {
                    handleError(result.throwable)
                    val errorMessage = result.throwable.message.orEmpty()
                    logExceptionToCrashlytics(errorMessage, result.throwable)
                }
            }
        }
    }

    private fun setAccordion(model: ShopNoteModel) {
        context?.let {
            val tpContent = Typography(it)
            tpContent.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            tpContent.text = MethodChecker.fromHtml(model.content)
            accordion?.addGroup(AccordionDataUnify(
                    model.title ?: "",
                    "",
                    null,
                    null,
                    tpContent,
                    false
            ).setContentPadding(16.toPx(), 4.toPx(), 16.toPx(), 4.toPx()))
        }
    }

    private fun getShopNotes() {
        buyerShopId?.run {
            viewModel?.getShopNotes(this)
            loader?.show()
            accordion?.gone()
        }
    }

    private fun handleError(throwable: Throwable) {
        when (throwable) {
            is SocketTimeoutException, is UnknownHostException, is ConnectException -> {
                view?.let {
                    showGlobalError(GlobalError.NO_CONNECTION)
                }
            }
            is RuntimeException -> {
                when (throwable.localizedMessage?.toIntOrNull()) {
                    ReponseStatus.GATEWAY_TIMEOUT, ReponseStatus.REQUEST_TIMEOUT -> showGlobalError(GlobalError.NO_CONNECTION)
                    ReponseStatus.NOT_FOUND -> showGlobalError(GlobalError.PAGE_NOT_FOUND)
                    ReponseStatus.INTERNAL_SERVER_ERROR -> showGlobalError(GlobalError.SERVER_ERROR)

                    else -> {
                        view?.let {
                            showGlobalError(GlobalError.SERVER_ERROR)
                        }
                    }
                }
            }
            else -> {
                view?.let {
                    showGlobalError(GlobalError.SERVER_ERROR)
                }
            }
        }
    }

    private fun showGlobalError(type: Int) {
        globalError?.setType(type)
        globalError?.setActionClickListener {
            globalError?.gone()
            getShopNotes()
        }
        accordion?.gone()
        globalError?.show()
    }
}
