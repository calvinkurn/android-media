package com.tokopedia.filter.newdynamicfilter

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.text.TextUtils
import android.view.View

import com.tokopedia.filter.common.data.Category
import com.tokopedia.filter.common.data.Option
import com.tokopedia.filter.R
import com.tokopedia.filter.newdynamicfilter.AbstractDynamicFilterDetailActivity.Companion.EXTRA_IS_USING_TRACKING
import com.tokopedia.filter.newdynamicfilter.AbstractDynamicFilterDetailActivity.Companion.EXTRA_TRACKING_DATA
import com.tokopedia.filter.newdynamicfilter.adapter.CategoryChildAdapter
import com.tokopedia.filter.newdynamicfilter.adapter.CategoryParentAdapter
import com.tokopedia.filter.newdynamicfilter.analytics.FilterTracking
import com.tokopedia.filter.newdynamicfilter.analytics.FilterTrackingData
import com.tokopedia.filter.newdynamicfilter.helper.OptionHelper

import org.parceler.Parcels



/**
 * Created by henrypriyono on 8/24/17.
 */

class DynamicFilterCategoryActivity : AppCompatActivity(), CategoryParentAdapter.OnItemClickListener, CategoryChildAdapter.OnItemClickListener {

    private var categoryList: List<Category>? = null
    private var rootRecyclerView: RecyclerView? = null
    private var childRecyclerView: RecyclerView? = null
    private var buttonClose: View? = null
    private var categoryParentAdapter: CategoryParentAdapter? = null
    private var categoryChildAdapter: CategoryChildAdapter? = null
    private var defaultCategoryId: String? = null
    private var defaultCategoryRootId: String? = null
    private var isUsingTracking: Boolean = false
    private var trackingData: FilterTrackingData? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dynamic_filter_category)
        fetchDataFromIntent()
        bindView()
        loadFilterItems()
    }

    private fun fetchDataFromIntent() {
        isUsingTracking = intent.getBooleanExtra(EXTRA_IS_USING_TRACKING, false)
        trackingData = intent.getParcelableExtra(EXTRA_TRACKING_DATA)
        defaultCategoryId = intent.getStringExtra(EXTRA_DEFAULT_CATEGORY_ID)
        defaultCategoryRootId = intent.getStringExtra(EXTRA_DEFAULT_CATEGORY_ROOT_ID)

        val optionList: List<Option> = Parcels.unwrap(
                intent.getParcelableExtra(EXTRA_OPTION_LIST))

        categoryList = OptionHelper.convertToCategoryList(optionList)
    }

    private fun bindView() {
        rootRecyclerView = findViewById(R.id.category_root_recyclerview)
        childRecyclerView = findViewById(R.id.category_child_recyclerview)
        buttonClose = findViewById(R.id.top_bar_close_button)
        buttonClose?.setOnClickListener {
            onBackPressed()
        }
    }

    private fun loadFilterItems() {
        if (TextUtils.isEmpty(defaultCategoryRootId)) {
            defaultCategoryRootId = categoryList?.get(0)?.id
        }

        categoryParentAdapter = CategoryParentAdapter(this, defaultCategoryRootId)
        categoryList?.let { categoryParentAdapter?.setDataList(it) }

        val rootLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rootRecyclerView?.layoutManager = rootLayoutManager

        rootRecyclerView?.adapter = categoryParentAdapter
        val defaultRootPosition = defaultCategoryRootId?.let { categoryParentAdapter?.getPositionById(it) }
        if (defaultRootPosition != null) {
            rootLayoutManager.scrollToPositionWithOffset(defaultRootPosition, DEFAULT_OFFSET)
        }

        categoryChildAdapter = CategoryChildAdapter(this)
        defaultCategoryId?.let { categoryChildAdapter?.setLastSelectedCategoryId(it) }
        categoryChildAdapter?.clear()
        defaultRootPosition?.let { categoryList?.get(it)?.children }?.let { categoryChildAdapter?.addAll(it) }

        val childLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        childRecyclerView?.layoutManager = childLayoutManager

        childRecyclerView?.adapter = categoryChildAdapter

        if (!TextUtils.isEmpty(defaultCategoryId)) {
            defaultCategoryId?.let { categoryChildAdapter?.toggleSelectedChildbyId(it) }
            categoryChildAdapter?.activePosition?.let { childLayoutManager.scrollToPositionWithOffset(it, DEFAULT_OFFSET) }
        }
    }

    override fun onItemClicked(category: Category, position: Int) {
        categoryParentAdapter?.activeId = category.id
        categoryParentAdapter?.notifyDataSetChanged()
        categoryChildAdapter?.clear()
        categoryChildAdapter?.addAll(category.children)
    }

    override fun onChildClicked(category: Category) {
        if (category.hasChild) {
            categoryChildAdapter?.toggleSelectedChildbyId(category.id)
        } else {
            if (isUsingTracking) {
                trackingData?.let {
                    FilterTracking.eventFilterJourney(
                            it,
                            resources.getString(R.string.title_category),
                            category.name, true, true, category.isAnnotation)
                }
            }
            applyFilter(category)
        }
    }

    private fun applyFilter(category: Category) {
        val intent = Intent()
        intent.putExtra(EXTRA_SELECTED_CATEGORY_ID, category.id)
        intent.putExtra(EXTRA_SELECTED_CATEGORY_NAME, category.name)
        intent.putExtra(EXTRA_SELECTED_CATEGORY_ROOT_ID, categoryParentAdapter?.activeId)
        setResult(RESULT_OK, intent)
        finish()
    }

    companion object {

        const val REQUEST_CODE = 221
        const val EXTRA_SELECTED_CATEGORY_ID = "EXTRA_SELECTED_CATEGORY_ID"
        const val EXTRA_SELECTED_CATEGORY_ROOT_ID = "EXTRA_SELECTED_CATEGORY_ROOT_ID"
        const val EXTRA_SELECTED_CATEGORY_NAME = "EXTRA_SELECTED_CATEGORY_NAME"

        private const val EXTRA_DEFAULT_CATEGORY_ID = "EXTRA_DEFAULT_CATEGORY_ID"
        private const val EXTRA_DEFAULT_CATEGORY_ROOT_ID = "EXTRA_DEFAULT_CATEGORY_ROOT_ID"
        private const val EXTRA_OPTION_LIST = "EXTRA_OPTION_LIST"
        private const val DEFAULT_OFFSET = 170

        fun moveTo(activity: AppCompatActivity?,
                   optionList: List<Option>,
                   defaultCategoryRootId: String,
                   defaultCategoryId: String,
                   isUsingTracking: Boolean,
                   trackingData: FilterTrackingData?
        ) {

            if (activity != null) {
                val intent = Intent(activity, DynamicFilterCategoryActivity::class.java)
                intent.putExtra(EXTRA_OPTION_LIST, Parcels.wrap(optionList))
                intent.putExtra(EXTRA_DEFAULT_CATEGORY_ROOT_ID, defaultCategoryRootId)
                intent.putExtra(EXTRA_DEFAULT_CATEGORY_ID, defaultCategoryId)
                intent.putExtra(EXTRA_IS_USING_TRACKING, isUsingTracking)
                intent.putExtra(EXTRA_TRACKING_DATA, trackingData)
                activity.startActivityForResult(intent, REQUEST_CODE)
            }
        }
    }
}
