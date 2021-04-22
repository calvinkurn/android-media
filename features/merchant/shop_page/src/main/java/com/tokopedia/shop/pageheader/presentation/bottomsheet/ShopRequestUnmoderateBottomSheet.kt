package com.tokopedia.shop.pageheader.presentation.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.shop.R
import com.tokopedia.shop.pageheader.presentation.adapter.ShopRequestUnmoderateBottomSheetAdapter
import com.tokopedia.shop.pageheader.presentation.adapter.ShopRequestUnmoderateBottomsheetViewHolderListener
import com.tokopedia.shop.pageheader.presentation.holder.ShopPageFragmentHeaderViewHolder
import com.tokopedia.shop.pageheader.presentation.holder.ShopPageFragmentViewHolderListener
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.LoaderUnify
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography

/**
 * author by Rafli Syam on 22/01/2021
 */
class ShopRequestUnmoderateBottomSheet : BottomSheetUnify(), ShopRequestUnmoderateBottomsheetViewHolderListener {

    companion object {
        @LayoutRes
        private val LAYOUT =  R.layout.shop_request_unmoderate_bottomsheet
        private val TAG = ShopRequestUnmoderateBottomSheet::class.java.simpleName

        fun createInstance() : ShopRequestUnmoderateBottomSheet = ShopRequestUnmoderateBottomSheet()
    }

    private var buttonSendUnmoderateRequest: UnifyButton? = null
    private var loaderModerateBottomsheet: LoaderUnify? = null
    private var typographyAlreadySentReq: Typography? = null
    private var bottomSheetListener : ShopPageFragmentViewHolderListener? = null
    private var rvRequestOptions: RecyclerView? = null
    private var choosenOptionValue = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setupBottomSheetChildView(inflater, container)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        initListener()
    }

    override fun setOptionValue(optionValue: String?) {
        optionValue?.let { choosenOptionValue = it }
    }

    fun init(listener: ShopPageFragmentViewHolderListener) {
        this.bottomSheetListener = listener
    }

    fun show(fragmentManager: FragmentManager?) {
        fragmentManager?.let {
            show(it, TAG)
        }
    }

    fun showLoading() {
        rvRequestOptions?.hide()
        loaderModerateBottomsheet?.show()
        buttonSendUnmoderateRequest?.isEnabled = false
    }

    fun setLoadingButton(state: Boolean) {
        buttonSendUnmoderateRequest?.isLoading = state
    }

    fun showOptionList() {
        rvRequestOptions?.show()
        typographyAlreadySentReq?.hide()
        buttonSendUnmoderateRequest?.isEnabled = true
        loaderModerateBottomsheet?.hide()
    }

    fun showModerateStatus() {
        rvRequestOptions?.hide()
        typographyAlreadySentReq?.show()
        buttonSendUnmoderateRequest?.isEnabled = false
        loaderModerateBottomsheet?.hide()
    }

    private fun setupBottomSheetChildView(inflater: LayoutInflater, container: ViewGroup?) {
        inflater.inflate(LAYOUT, container).apply {
            rvRequestOptions = findViewById(R.id.rv_req_unmoderate_options)
            buttonSendUnmoderateRequest = findViewById(R.id.btn_send_req_unmoderate)
            loaderModerateBottomsheet = findViewById(R.id.loader_moderate_bottomsheet)
            typographyAlreadySentReq = findViewById(R.id.tv_already_sent_request)
            setTitle(getString(R.string.shop_page_header_request_unmoderate_bottomsheet_title))
            setChild(this)
            setCloseClickListener {
                dismiss()
            }
        }
    }

    private fun initRecyclerView() {
        rvRequestOptions?.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = ShopRequestUnmoderateBottomSheetAdapter(context, this@ShopRequestUnmoderateBottomSheet)
        }
    }

    private fun initListener() {
        buttonSendUnmoderateRequest?.setOnClickListener {
            // send request unmoderate shop
            buttonSendUnmoderateRequest?.isLoading = true
            bottomSheetListener?.onSendRequestOpenModerate(choosenOptionValue)
        }
    }

}