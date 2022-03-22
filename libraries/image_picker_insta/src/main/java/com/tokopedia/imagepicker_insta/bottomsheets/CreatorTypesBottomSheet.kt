package com.tokopedia.imagepicker_insta.bottomsheets

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.imagepicker_insta.R
import com.tokopedia.imagepicker_insta.models.CreatorListData
import com.tokopedia.imagepicker_insta.views.adapters.ContentCreatorListAdapter
import com.tokopedia.kotlin.extensions.view.getScreenHeight
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.toDp
import kotlinx.android.synthetic.main.imagepicker_insta_content_creator_type_bottom_sheet.*

class CreatorTypesBottomSheet : BottomSheetUnify() {

    private val childLayoutRes = R.layout.imagepicker_insta_content_creator_type_bottom_sheet
    private var creatorListData: List<CreatorListData>? = null
    private val createrListAdapter: ContentCreatorListAdapter by lazy {
        ContentCreatorListAdapter()
    }
//    private var listener: CreateContentPostCommonListener? = null

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
        setTitle(getString(R.string.imagepicker_content_creator_bottom_sheet_header))
    }


    private fun setDefaultParams() {
        showCloseIcon = true
        showHeader = true
        customPeekHeight = (getScreenHeight() / 2).toDp()
    }


    private fun initAdapter() {
        creatorListData?.let {
            createrListAdapter.updateProduct(it)
            content_creator_rv.adapter = createrListAdapter
            content_creator_rv.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        }
    }



    fun show(
        bundle: Bundle,
        childFragmentManager: FragmentManager,
        productList: List<CreatorListData>,
    ) {
        arguments = bundle
        val tag = "ContentCreationProductTagBottomSheet"
        this.creatorListData = productList
//        this.listener = listener
        show(childFragmentManager, tag)

    }
}