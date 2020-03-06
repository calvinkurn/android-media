package com.tokopedia.browse.categoryNavigation.fragments


import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.applink.RouteManager

import com.tokopedia.browse.R
import com.tokopedia.browse.categoryNavigation.adapters.CategoryLevelTwoAdapter
import com.tokopedia.browse.categoryNavigation.data.model.newcategory.CategoryChildItem
import com.tokopedia.browse.categoryNavigation.di.CategoryNavigationComponent
import com.tokopedia.browse.categoryNavigation.di.DaggerCategoryNavigationComponent
import com.tokopedia.browse.categoryNavigation.utils.Constants
import com.tokopedia.browse.categoryNavigation.view.ActivityStateListener
import com.tokopedia.browse.categoryNavigation.viewmodel.CategoryLevelTwoViewModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.fragment_category_level_two.*
import javax.inject.Inject

class CategoryLevelTwoFragment : Fragment(), Listener, HasComponent<CategoryNavigationComponent> {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var categoryLevelTwoViewModel: CategoryLevelTwoViewModel

    private lateinit var categoryLevelTwoAdapter: CategoryLevelTwoAdapter
    private lateinit var gridLayoutManager: GridLayoutManager

    private val childList = ArrayList<CategoryChildItem>()
    private val defaultCaseId = "0"

    private var currentPosition = "0"

    private var categoryApplink: String? = null
    private var currentCategoryName: String = ""

    var activityStateListener: ActivityStateListener? = null

    companion object {
        @JvmStatic
        fun newInstance() = CategoryLevelTwoFragment()
    }


    override fun getComponent(): CategoryNavigationComponent = DaggerCategoryNavigationComponent.builder().baseAppComponent((activity?.applicationContext as BaseMainApplication).baseAppComponent).build()


    override fun refreshView(id: String, categoryName: String, applink: String?) {
        currentPosition = id
        currentCategoryName = categoryName
        categoryLevelTwoViewModel.refresh(id)
        setShimmer(id)
        categoryApplink = applink
    }

    private fun setShimmer(id: String) {
        slave_list.visibility = View.GONE

    }

    private fun removeShimmer() {

        if (currentPosition != defaultCaseId) {
           // label_lihat_semua.visibility = View.VISIBLE
            //hotlist.visibility = View.VISIBLE
            // hotlist_name.visibility = View.VISIBLE
        }


        slave_list.visibility = View.VISIBLE

        if (categoryApplink != null) {

          /*  label_lihat_semua.setOnClickListener {
                routeToCategoryLevelTwo(activity, categoryApplink ?: "")
            }
            category_name.setOnClickListener {
                routeToCategoryLevelTwo(activity, categoryApplink ?: "")
            }*/
        }
    }

    private fun routeToCategoryLevelTwo(context: Context?, categoryApplink: String) {
        context?.let {
            RouteManager.route(it, categoryApplink)
           // CategoryAnalytics.createInstance().eventClickLihatSemua(label_lihat_semua.text.toString())
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        component.inject(this)
        return inflater.inflate(R.layout.fragment_category_level_two, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModel()
        initView()
        setUpObserver()
    }

    private fun setUpObserver() {
        categoryLevelTwoViewModel.getCategoryChildren().observe(viewLifecycleOwner, Observer {

            when (it) {
                is Success -> {
                   // empty_view_second_level.visibility = View.GONE
                    childList.clear()
                    childList.addAll(it.data)
                    removeShimmer()
                    slave_list.adapter = CategoryLevelTwoAdapter(childList, activityStateListener?.getActivityTrackingQueue())
                }

                is Fail -> {
                   /* shimmer_layout_default.visibility = View.GONE
                    shimmer_layout.visibility = View.GONE
                    empty_view_second_level.visibility = View.VISIBLE*/

                }
            }

        })

    }

    private fun initView() {

      /*  empty_view_second_level.setOnClickListener {
            categoryLevelTwoViewModel.refresh(currentPosition)

        }*/

        categoryLevelTwoAdapter = CategoryLevelTwoAdapter(childList, activityStateListener?.getActivityTrackingQueue())
        gridLayoutManager = GridLayoutManager(context, 6, GridLayoutManager.VERTICAL, false)

        gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return when (slave_list.adapter?.getItemViewType(position)) {
                    Constants.ProductHeaderView -> 6
                    Constants.YangLagiHitsView -> 3
                    Constants.ProductView -> 2
                    else -> 6
                }

            }
        }


        slave_list.layoutManager = gridLayoutManager
        slave_list.adapter = categoryLevelTwoAdapter
        //slave_list.isNestedScrollingEnabled = false


        /*  val hotlistAdapter = HotlistAdapter(categoryHotlist, activityStateListener?.getActivityTrackingQueue())
          val horizontalLayout = GridLayoutManager(context, 1, GridLayoutManager.HORIZONTAL, false)
          hotlist.layoutManager = horizontalLayout
          hotlist.adapter = hotlistAdapter
          hotlist.isNestedScrollingEnabled = false*/


    }

    private fun initViewModel() {
        val viewModelProvider = ViewModelProviders.of(this, viewModelFactory)
        categoryLevelTwoViewModel = viewModelProvider.get(CategoryLevelTwoViewModel::class.java)
    }

    fun startShimmer(isStarted: Boolean) {
      /*  if (isStarted) {
            if (currentPosition == defaultCaseId) {
                shimmer_layout_default.visibility = View.VISIBLE
                shimmer_layout.visibility = View.GONE
            } else {
                shimmer_layout_default.visibility = View.GONE
                shimmer_layout.visibility = View.VISIBLE
            }
        } else {
            shimmer_layout_default.visibility = View.GONE
            shimmer_layout.visibility = View.GONE
        }*/

    }
}

interface Listener {
    fun refreshView(id: String, categoryName: String, applink: String?)
}
