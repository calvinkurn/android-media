package com.tokopedia.shop.note.view.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.shop.R
import com.tokopedia.shop.ShopComponentHelper
import com.tokopedia.shop.common.constant.ShopParamConstant
import com.tokopedia.shop.common.graphql.data.shopnote.ShopNoteModel
import com.tokopedia.shop.common.util.TextHtmlUtils
import com.tokopedia.shop.databinding.FragmentShopNoteDetailBinding
import com.tokopedia.shop.note.NoteUtil
import com.tokopedia.shop.note.di.component.DaggerShopNoteComponent
import com.tokopedia.shop.note.di.module.ShopNoteModule
import com.tokopedia.shop.note.view.presenter.ShopNoteDetailViewModel
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.utils.view.binding.viewBinding
import javax.inject.Inject

class ShopNoteDetailFragment: BaseDaggerFragment() {

    private var shopNoteDetailViewModel: ShopNoteDetailViewModel? = null
    private var shopNoteId: String = ""
    private var shopId: String = ""
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private var viewErrorState: View? = null
    private var mainContainer: View? = null
    private var viewLoadingState: View? = null
    private var textViewDate: Typography? = null
    private var textViewDesc: Typography? = null
    private val viewBinding: FragmentShopNoteDetailBinding? by viewBinding()

    companion object {
        private const val VIEW_CONTENT = 1
        private const val VIEW_LOADING = 2
        private const val VIEW_ERROR = 3
        @JvmStatic
        fun newInstance(shopId: String, noteId: String): Fragment = ShopNoteDetailFragment().apply {
            arguments = Bundle().apply {
                putString(ShopParamConstant.EXTRA_SHOP_ID, shopId)
                putString(ShopParamConstant.EXTRA_SHOP_NOTE_ID, noteId)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        shopNoteId = arguments?.getString(ShopParamConstant.EXTRA_SHOP_NOTE_ID) ?: ""
        shopId = arguments?.getString(ShopParamConstant.EXTRA_SHOP_ID) ?: ""
        shopNoteDetailViewModel = ViewModelProviders.of(this, viewModelFactory).get(ShopNoteDetailViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_shop_note_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initView()
        getShopDetail()
        observeLiveData()
    }

    private fun initView() {
        viewErrorState = viewBinding?.viewErrorState
        mainContainer = viewBinding?.mainContainer
        viewLoadingState = viewBinding?.viewLoadingState?.root
        textViewDate = viewBinding?.textViewDate
        textViewDesc = viewBinding?.textViewDesc
    }

    private fun observeLiveData() {
        shopNoteDetailViewModel?.shopNoteDetailData?.observe(viewLifecycleOwner, Observer {
            when(it){
                is Success -> {
                    onSuccessGetShopNoteList(it.data)
                }
                is Fail ->{
                    onErrorGetShopNoteList(it.throwable)
                }
            }
        })
    }

    override fun onDestroy() {
        shopNoteDetailViewModel?.shopNoteDetailData?.removeObservers(this)
        super.onDestroy()
    }

    override fun getScreenName(): String? = null

    private fun getShopDetail(){
        setViewState(VIEW_LOADING)
        shopNoteDetailViewModel?.getShopNoteList(shopId,shopNoteId)
    }

    private fun setViewState(viewState: Int) {
        when(viewState){
            VIEW_LOADING -> {
                showLoadingView()
            }
            VIEW_ERROR -> {
                showErrorView()
            }
            VIEW_CONTENT -> {
                showContentView()
            }
        }
    }

    private fun showLoadingView() {
        viewErrorState?.hide()
        mainContainer?.hide()
        viewLoadingState?.show()
    }

    private fun showContentView() {
        viewLoadingState?.hide()
        viewErrorState?.hide()
        mainContainer?.show()
    }

    private fun showErrorView() {
        viewLoadingState?.hide()
        mainContainer?.hide()
        viewErrorState?.show()
    }

    override fun initInjector() {
        activity?.let {
            DaggerShopNoteComponent
                    .builder()
                    .shopNoteModule(ShopNoteModule())
                    .shopComponent(ShopComponentHelper().getComponent(it.application, it))
                    .build()
                    .inject(this)
        }
    }

    //    we can't use viewbinding for the code below, since the layout from abstraction hasn't implement viewbinding
    private fun onErrorGetShopNoteList(e: Throwable?) {
        setViewState(VIEW_ERROR)
        val textRetryError = viewErrorState?.findViewById<TextView>(com.tokopedia.abstraction.R.id.message_retry)
        val buttonRetryError = viewErrorState?.findViewById<TextView>(com.tokopedia.abstraction.R.id.button_retry)
        textRetryError?.text = ErrorHandler.getErrorMessage(activity, e)
        buttonRetryError?.setOnClickListener { getShopDetail() }
    }

    private fun onSuccessGetShopNoteList(shopNoteDetail: ShopNoteModel?) {
        setViewState(VIEW_CONTENT)
        shopNoteDetail?.run {
            (activity as AppCompatActivity).supportActionBar?.title = shopNoteDetail.title
            val latestUpdate  = shopNoteDetail.updateTimeUtc.toIntOrZero()
            textViewDate?.text = getString(
                    R.string.shop_note_detail_date_format,
                    NoteUtil.convertUnixToFormattedDate(latestUpdate),
                    NoteUtil.convertUnixToFormattedTime(latestUpdate)
            )
            textViewDesc?.text = TextHtmlUtils.getTextFromHtml(shopNoteDetail.content)
        }
    }
}