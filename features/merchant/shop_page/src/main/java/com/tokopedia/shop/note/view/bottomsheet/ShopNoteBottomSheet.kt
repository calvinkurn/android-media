package com.tokopedia.shop.note.view.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.globalerror.ReponseStatus
import com.tokopedia.kotlin.extensions.view.getScreenHeight
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.shop.R
import com.tokopedia.shop.ShopComponentHelper
import com.tokopedia.shop.common.graphql.data.shopnote.ShopNoteModel
import com.tokopedia.shop.common.util.ShopPageExceptionHandler.logExceptionToCrashlytics
import com.tokopedia.shop.info.di.component.DaggerShopInfoComponent
import com.tokopedia.shop.info.di.module.ShopInfoModule
import com.tokopedia.shop.note.data.ShopNoteBottomSheetUiModel
import com.tokopedia.shop.note.view.adapter.ShopNoteBottomSheetAdapter
import com.tokopedia.shop.note.view.viewmodel.ShopNoteBottomSheetViewModel
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.LoaderUnify
import com.tokopedia.unifycomponents.toDp
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

class ShopNoteBottomSheet : BottomSheetUnify() {

    companion object {
        val TAG = ShopNoteBottomSheet::class.java.simpleName
        private const val SHOP_ID = "EXTRA_SHOP_ID"
        private val LAYOUT = R.layout.fragment_shop_note_bottom_sheet

        @JvmStatic
        fun createInstance(shopId: String?) = ShopNoteBottomSheet().apply {
            arguments = Bundle().apply {
                putString(SHOP_ID, shopId)
            }
        }
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var viewModel: ShopNoteBottomSheetViewModel

    private var buyerShopId: String? = null
    private var rvNote: RecyclerView? = null
    private var adapter: ShopNoteBottomSheetAdapter? = null
    private var loader: LoaderUnify? = null
    private var globalError: GlobalError? = null

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
        LayoutInflater.from(context).inflate(LAYOUT, null)?.apply {
            loader = findViewById(R.id.loader)
            rvNote = findViewById(R.id.rv_note)
            globalError = findViewById(R.id.global_error)
            setTitle(getString(R.string.shop_note_title))
            setChild(this)
            setCloseClickListener {
                dismiss()
            }
        }

        adapter = ShopNoteBottomSheetAdapter()
        rvNote?.adapter = adapter
        rvNote?.layoutManager = LinearLayoutManager(context)

        showKnob = true
        showCloseIcon = !showKnob
        isDragable = showKnob
        isHideable = showKnob
        clearContentPadding = showKnob
        customPeekHeight = (getScreenHeight() / 2).toDp()
    }

    private fun initInjector() {
        activity?.run {
            DaggerShopInfoComponent
                    .builder()
                    .shopInfoModule(ShopInfoModule())
                    .shopComponent(ShopComponentHelper().getComponent(application, this))
                    .build()
                    .inject(this@ShopNoteBottomSheet)
        }
    }

    private fun setupObserver() {
        viewModel.shopNotes.observe(viewLifecycleOwner) { result ->
            loader?.gone()
            rvNote?.show()
            when(result) {
                is Success -> {
                    adapter?.setItemsAndAnimateChanges(mapToShopNoteUiModel(result.data))
                }
                is Fail -> {
                    handleError(result.throwable)
                    result.throwable.message?.let { logExceptionToCrashlytics(it, result.throwable) }
                }
            }
        }
    }

    private fun getShopNotes() {
        buyerShopId?.run {
            viewModel.getShopNotes(this)
            loader?.show()
            rvNote?.gone()
        }
    }

    private fun mapToShopNoteUiModel(response: List<ShopNoteModel>): List<ShopNoteBottomSheetUiModel> {
        val notes = mutableListOf<ShopNoteBottomSheetUiModel>()
        val responseSize = response.size - 1
        response.forEachIndexed { position, model ->
            notes.add(
                    ShopNoteBottomSheetUiModel(
                            title = model.title ?: "",
                            description = model.content ?: "",
                            isTheLastPosition = position == responseSize
                    )
            )
        }
        return notes
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
        rvNote?.gone()
        globalError?.show()
    }
}