package com.tokopedia.createpost.view.bottomSheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.createpost.createpost.R
import com.tokopedia.createpost.view.listener.CreateContentPostCOmmonLIstener
import com.tokopedia.createpost.view.adapter.CreatePostTagAdapter
import com.tokopedia.createpost.view.viewmodel.RelatedProductItem
import com.tokopedia.kotlin.extensions.view.getScreenHeight
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.toDp
import kotlinx.android.synthetic.main.content_creation_product_tag_bottom_sheet.*

class ContentCreationProductTagBottomSheet : BottomSheetUnify() {

    private val childLayoutRes = R.layout.content_creation_product_tag_bottom_sheet
    private var productData: List<RelatedProductItem>? = null
    private val createPostTagAdapter: CreatePostTagAdapter by lazy {
        CreatePostTagAdapter(onDeleteProduct = this::onDeleteProduct)
    }
    private lateinit var listener: CreateContentPostCOmmonLIstener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setDefaultParams()
        initBottomSheet()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initAdapter()
    }

    private fun initBottomSheet() {
        val childView = LayoutInflater.from(context).inflate(childLayoutRes,
            null, false)
        setChild(childView)
        setTitle(getString(R.string.feed_content_product_bottom_sheet_name))
    }


    private fun setDefaultParams() {
        showCloseIcon = true
        showHeader = true
        customPeekHeight = (getScreenHeight() / 2).toDp()
    }


    private fun initAdapter() {
        productData?.let {
            createPostTagAdapter.updateProduct(it)
            rv_product_tag.adapter = createPostTagAdapter
            rv_product_tag.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        }
    }

    private fun onDeleteProduct(position: Int) {
        listener.deleteItemFromProductTagList(position)
    }


    private fun openUrlWebView(urlString: String) {
        val webViewAppLink = ApplinkConst.WEBVIEW + "?url=" + urlString
        RouteManager.route(context, webViewAppLink)
    }

    fun show(
        bundle: Bundle,
        childFragmentManager: FragmentManager,
        productList: List<RelatedProductItem>,
        listener: CreateContentPostCOmmonLIstener,
    ) {
        arguments = bundle
        val tag = "ContentCreationProductTagBottomSheet"
        this.productData = productList
        this.listener = listener
        show(childFragmentManager, tag)

    }

}

