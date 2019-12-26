package com.tokopedia.browse.categoryNavigation.fragments

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.common.di.component.HasComponent

import com.tokopedia.browse.R
import com.tokopedia.browse.categoryNavigation.adapters.CategoryLevelOneAdapter
import com.tokopedia.browse.categoryNavigation.data.model.category.CategoriesItem
import com.tokopedia.browse.categoryNavigation.di.CategoryNavigationComponent
import com.tokopedia.browse.categoryNavigation.di.DaggerCategoryNavigationComponent
import com.tokopedia.browse.categoryNavigation.view.ActivityStateListener
import com.tokopedia.browse.categoryNavigation.view.CategoryChangeListener
import com.tokopedia.browse.categoryNavigation.viewmodel.CategoryLevelOneViewModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.fragment_categorylevel_one.*
import javax.inject.Inject
import kotlin.collections.ArrayList


class CategorylevelOneFragment : Fragment(), HasComponent<CategoryNavigationComponent> {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    lateinit var categoryBrowseViewModel: CategoryLevelOneViewModel
    private val categoryList = ArrayList<CategoriesItem>()

    var activityStateListener: ActivityStateListener? = null

    var selectedPosition = 0

    var selectedItemIdentifier: String? = null

    companion object {
        private val EXTRA_CATEGORY_NAME = "CATEGORY_NAME"
        @JvmStatic
        fun newInstance(categoryName: String?): CategorylevelOneFragment {
            val fragment = CategorylevelOneFragment()
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
        if (arguments != null && arguments!!.containsKey(EXTRA_CATEGORY_NAME)) {
            selectedItemIdentifier = arguments!!.getString(EXTRA_CATEGORY_NAME)
        }
        initView()
        setUpObserver()
    }

    private fun initView() {
        val linearLayoutManager = LinearLayoutManager(context)
        master_list.layoutManager = linearLayoutManager
        val categoryLevelOneAdapter = CategoryLevelOneAdapter(categoryList, listener, activityStateListener?.getActivityTrackingQueue())
        master_list.adapter = categoryLevelOneAdapter

            val viewModelProvider = ViewModelProviders.of(this, viewModelFactory)
            categoryBrowseViewModel = viewModelProvider.get(CategoryLevelOneViewModel::class.java)
            categoryBrowseViewModel.bound()
    }

    private fun setUpObserver() {
        categoryBrowseViewModel.getCategoryList().observe(this, Observer {

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
                }
                is Fail -> {
                    (activity as CategoryChangeListener).onError()
                }
            }

        })
    }

    private fun getPositionFromIdentifier(categoryList: ArrayList<CategoriesItem>): Int {
        for (i in 0 until categoryList.size) {
            if (categoryList[i].identifier!!.equals(selectedItemIdentifier)) {
                selectedPosition = i
                return i
            }
        }

        return 0
    }


    fun reloadData() {
        categoryBrowseViewModel.bound()
    }

    private fun initiateSlaveFragmentLoading(position: Int) {
        if (activity != null) {
            (activity as CategoryChangeListener).onCategoryChanged(categoryList[position].id!!, categoryList[position].name!!, categoryList[position].applinks)
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
}


