package com.tokopedia.shop.note.view.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.network.ErrorHandler
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.shop.R
import com.tokopedia.shop.ShopComponentHelper
import com.tokopedia.shop.common.constant.ShopParamConstant
import com.tokopedia.shop.common.graphql.data.shopnote.ShopNoteModel
import com.tokopedia.shop.common.util.TextHtmlUtils
import com.tokopedia.shop.note.NoteUtil
import com.tokopedia.shop.note.di.component.DaggerShopNoteComponent
import com.tokopedia.shop.note.di.module.ShopNoteModule
import com.tokopedia.shop.note.view.listener.ShopNoteDetailView
import com.tokopedia.shop.note.view.presenter.ShopNoteDetailPresenter
import kotlinx.android.synthetic.main.fragment_shop_note_detail.*
import javax.inject.Inject

class ShopNoteDetailFragment: BaseDaggerFragment(), ShopNoteDetailView {

    @Inject lateinit var shopNoteDetailPresenter: ShopNoteDetailPresenter
    private var shopNoteId: String = ""
    private var shopId: String = ""

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
        shopNoteDetailPresenter.attachView(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_shop_note_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        getShopDetail()
    }

    override fun onDestroy() {
        super.onDestroy()
        shopNoteDetailPresenter.detachView()
    }

    override fun getScreenName(): String? = null

    private fun getShopDetail(){
        setViewState(VIEW_LOADING)
        shopNoteDetailPresenter.getShopNoteList(shopId,shopNoteId)
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
        view_error_state?.hide()
        main_container?.hide()
        view_loading_state?.show()
    }

    private fun showContentView() {
        view_loading_state?.hide()
        view_error_state?.hide()
        main_container?.show()
    }

    private fun showErrorView() {
        view_loading_state?.hide()
        main_container?.hide()
        view_error_state?.show()
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

    override fun onErrorGetShopNoteList(e: Throwable?) {
        setViewState(VIEW_ERROR)
        val textRetryError = view_error_state.findViewById<TextView>(com.tokopedia.abstraction.R.id.message_retry)
        val buttonRetryError = view_error_state.findViewById<TextView>(com.tokopedia.abstraction.R.id.button_retry)
        textRetryError.text = ErrorHandler.getErrorMessage(activity, e)
        buttonRetryError.setOnClickListener { getShopDetail() }
    }

    override fun onSuccessGetShopNoteList(shopNoteDetail: ShopNoteModel?) {
        setViewState(VIEW_CONTENT)
        shopNoteDetail?.run {
            (activity as AppCompatActivity).supportActionBar?.title = shopNoteDetail.title
            val latestUpdate  = shopNoteDetail.updateTimeUtc.toIntOrZero()
            textViewDate.text = getString(
                    R.string.shop_note_detail_date_format,
                    NoteUtil.convertUnixToFormattedDate(latestUpdate),
                    NoteUtil.convertUnixToFormattedTime(latestUpdate)
            )
            textViewDesc.text = TextHtmlUtils.getTextFromHtml(shopNoteDetail.content)
        }
    }
}