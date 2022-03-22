package com.tokopedia.createpost.view.bottomSheet

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.createpost.createpost.R
import com.tokopedia.createpost.view.adapter.SearchTypeListAdapter
import com.tokopedia.createpost.view.listener.SearchCategoryBottomSheetListener
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.getScreenHeight
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.toDp
import kotlinx.android.synthetic.main.content_creation_search_type_bottom_sheet.*

class SearchCategoryTypeBottomSheet : BottomSheetUnify() {

    private val childLayoutRes = R.layout.content_creation_search_type_bottom_sheet
    private lateinit var searchTypeDataList: List<SearchTypeData>

    private var listener: SearchCategoryBottomSheetListener? = null
    private lateinit var searchListAdapter: SearchTypeListAdapter


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
        setTitle(getString(R.string.content_creation_search_bs_header))
    }


    private fun setDefaultParams() {
        showCloseIcon = true
        showHeader = true
        customPeekHeight = (getScreenHeight() / 2).toDp()
    }


    private fun initAdapter() {

        if (::searchTypeDataList.isInitialized) {
            listener?.let {
                searchListAdapter = SearchTypeListAdapter(searchTypeDataList, listener!!)
            }
            content_creator_search_bs_rv.adapter = searchListAdapter
            content_creator_search_bs_rv.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        }
    }


    fun show(
        bundle: Bundle,
        childFragmentManager: FragmentManager,
        listener: SearchCategoryBottomSheetListener,
        shopName: String,
        shopBadge: String,
        context: Context
    ) {
        arguments = bundle
        val tag = "ContentCreationSearchTypeBottomSheet"
        this.searchTypeDataList = createList(shopName, shopBadge, context)
        this.listener = listener
        show(childFragmentManager, tag)

    }
    private fun createList(shopName: String, shopBadge: String, context: Context): List<SearchTypeData> {
        var list = mutableListOf<SearchTypeData>()
        context.let {
            list.apply {
                add(
                    SearchTypeData(
                        id = 0,
                        text = it.getString(R.string.content_creation_search_bs_item_tokopedia),
                        type = CreatorListItemType.TYPE_CONTENT,
                        iconId = IconUnify.TOPED
                    )
                )
                add(
                    SearchTypeData(
                        id = 1,
                        text = it.getString(R.string.content_creation_search_bs_header_item_tokopedia),
                        type = CreatorListItemType.TYPE_HEADER
                    )
                )
                add(
                    SearchTypeData(
                        id = 2,
                        text = it.getString(R.string.content_creation_search_bs_item_terakhir),
                        type = CreatorListItemType.TYPE_CONTENT,
                        iconId = IconUnify.CART
                    )
                )
                add(
                    SearchTypeData(
                        id = 3,
                        text = it.getString(R.string.content_creation_search_bs_item_wishlist),
                        type = CreatorListItemType.TYPE_CONTENT,
                        iconId = IconUnify.HEART

                    )
                )
                add(
                    SearchTypeData(
                        id = 4,
                        text = it.getString(R.string.content_creation_search_bs_header_item_shop),
                        type = CreatorListItemType.TYPE_HEADER
                    )
                )
                add(
                    SearchTypeData(
                        id = 5,
                        text = shopName,
                        type = CreatorListItemType.TYPE_CONTENT,
                        iconUrl = shopBadge

                    )
                )
            }
        }
       return list
    }
}