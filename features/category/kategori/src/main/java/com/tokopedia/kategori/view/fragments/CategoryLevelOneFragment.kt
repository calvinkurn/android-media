package com.tokopedia.kategori.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.kategori.R
import com.tokopedia.kategori.adapters.CategoryLevelOneAdapter
import com.tokopedia.kategori.di.CategoryNavigationComponent
import com.tokopedia.kategori.di.DaggerCategoryNavigationComponent
import com.tokopedia.common_category.data.kategorymodel.CategoriesItem
import com.tokopedia.kategori.view.PerformanceMonitoringListener
import com.tokopedia.kategori.view.activity.ActivityStateListener
import com.tokopedia.kategori.view.activity.CategoryChangeListener
import com.tokopedia.kategori.viewmodel.CategoryLevelOneViewModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.fragment_categorylevel_one.*
import javax.inject.Inject

private const val EXTRA_CATEGORY_NAME = "CATEGORY_NAME"

class CategoryLevelOneFragment : Fragment(), HasComponent<CategoryNavigationComponent> {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var categoryBrowseViewModel: CategoryLevelOneViewModel
    private val categoryList = ArrayList<CategoriesItem>()

    var activityStateListener: ActivityStateListener? = null

    var performanceMonitoringListener: PerformanceMonitoringListener? = null

    private var selectedPosition = 0

    private var selectedItemIdentifier: String? = null

    private lateinit var categoryLevelOneAdapter: CategoryLevelOneAdapter

    companion object {
        @JvmStatic
        fun newInstance(categoryName: String?): CategoryLevelOneFragment {
            val fragment = CategoryLevelOneFragment()
            if (categoryName != null) {
                val bundle = Bundle()
                bundle.putString(EXTRA_CATEGORY_NAME, categoryName)
                fragment.arguments = bundle
            }
            return fragment
        }

    }

    override fun getComponent(): CategoryNavigationComponent = DaggerCategoryNavigationComponent.builder().baseAppComponent((activity?.applicationContext as BaseMainApplication).baseAppComponent).build()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_categorylevel_one, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        component.inject(this)
        arguments?.let {
            if (it.containsKey(EXTRA_CATEGORY_NAME)) {
                selectedItemIdentifier = it.getString(EXTRA_CATEGORY_NAME)
            }
        }
        initView()
        setUpObserver()
    }

    private fun initView() {
        val linearLayoutManager = LinearLayoutManager(context)
        master_list.layoutManager = linearLayoutManager
        addShimmerItems(categoryList)
        categoryLevelOneAdapter = CategoryLevelOneAdapter(categoryList, listener, activityStateListener?.getActivityTrackingQueue())
        master_list.adapter = categoryLevelOneAdapter

        val viewModelProvider = ViewModelProviders.of(this, viewModelFactory)
        categoryBrowseViewModel = viewModelProvider.get(CategoryLevelOneViewModel::class.java)
        categoryBrowseViewModel.bound(performanceMonitoringListener)
    }

    private fun addShimmerItems(categoryList: ArrayList<CategoriesItem>) {
        // adding shimmer elements in recyclerview
        val item = CategoriesItem()
        item.type = 0
        for (i in 0..8) {
            categoryList.add(item)
        }
    }

    private fun setUpObserver() {
        categoryBrowseViewModel.getCategoryList().observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> {
                    categoryList.clear()
                    categoryList.addAll(it.data.categories as ArrayList<CategoriesItem>)
                    var selectedPosition = 0
                    if (selectedItemIdentifier != null) {
                        selectedPosition = getPositionFromIdentifier(categoryList)
                        categoryList[selectedPosition].isSelected = true
                    } else {
                        categoryList[selectedPosition].isSelected = true
                    }
                    master_list.adapter?.notifyDataSetChanged()
                    master_list.layoutManager?.scrollToPosition(selectedPosition)
                    initiateSlaveFragmentLoading(selectedPosition)
                    stopCategoryPagePerformanceMonitoring()
                }
                is Fail -> {
                    (activity as CategoryChangeListener).onError(it.throwable)
                }
            }

        })
    }

    private fun stopCategoryPagePerformanceMonitoring() {
        master_list.viewTreeObserver
                .addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
                    override fun onGlobalLayout() {
                        performanceMonitoringListener?.stopRenderPerformanceMonitoring()
                        performanceMonitoringListener?.stopPerformanceMonitoring()
                        master_list.viewTreeObserver.removeOnGlobalLayoutListener(this)
                    }
                })
    }

    private fun getPositionFromIdentifier(categoryList: ArrayList<CategoriesItem>): Int {
        for (i in 0 until categoryList.size) {
            categoryList[i].identifier?.let {
                if (it == selectedItemIdentifier) {
                    selectedPosition = i
                    return i
                }
            }
        }
        return 0
    }


    fun reloadData() {
        categoryBrowseViewModel.bound(performanceMonitoringListener)
    }

    private fun initiateSlaveFragmentLoading(position: Int) {
        if (activity != null) {
            (activity as CategoryChangeListener).onCategoryChanged(categoryList[position].id
                    ?: "", categoryList[position].name ?: "", categoryList[position].applinks)
        }
    }

    private var listener = object : CategorySelectListener {
        override fun onItemClicked(id: String, position: Int, categoryName: String, applink: String?) {
            if (position != selectedPosition) {
                categoryList[selectedPosition].isSelected = false
                categoryList[position].isSelected = true
                master_list.adapter?.notifyItemChanged(position)
                master_list.adapter?.notifyItemChanged(selectedPosition)
                selectedPosition = position
                if (activity != null)
                    (activity as CategoryChangeListener).onCategoryChanged(id, categoryName, applink)
            }
        }
    }


    interface CategorySelectListener {
        fun onItemClicked(id: String, position: Int, categoryName: String, applink: String?)
    }

    override fun onPause() {
        categoryLevelOneAdapter.onPause()
        super.onPause()
    }
}
