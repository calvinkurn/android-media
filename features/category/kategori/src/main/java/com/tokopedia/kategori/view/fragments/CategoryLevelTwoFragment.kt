package com.tokopedia.kategori.view.fragments


import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.kategori.adapters.CategoryLevelTwoAdapter
import com.tokopedia.kategori.model.CategoryChildItem
import com.tokopedia.kategori.di.CategoryNavigationComponent
import com.tokopedia.kategori.di.DaggerCategoryNavigationComponent
import com.tokopedia.kategori.Constants
import com.tokopedia.kategori.R
import com.tokopedia.kategori.view.activity.ActivityStateListener
import com.tokopedia.kategori.viewmodel.CategoryLevelTwoViewModel
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
    private var currentPosition = "0"
    private var categoryApplink: String? = null
    private var currentCategoryName: String = ""

    private var totalSpanCount = 6
    private var fullItemSpan = 6
    private var halfItemSpan = 3
    private var oneThirdSpan = 2

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
        categoryApplink = applink
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        component.inject(this)
        return inflater.inflate(R.layout.fragment_category_level_two, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModel()
        setUpObserver()
        initView()
    }

    private fun setUpObserver() {
        categoryLevelTwoViewModel.getLevelTwoList().observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> {
                    categoryLevelTwoAdapter.pushTrackingEvents()
                    categoryLevelTwoAdapter = CategoryLevelTwoAdapter(it.data.toMutableList(), activityStateListener?.getActivityTrackingQueue())
                    slave_list.adapter = categoryLevelTwoAdapter
                }
            }

        })

    }

    private fun initView() {
        addShimmerItems(childList)
        categoryLevelTwoAdapter = CategoryLevelTwoAdapter(childList, activityStateListener?.getActivityTrackingQueue())
        gridLayoutManager = GridLayoutManager(context, totalSpanCount, GridLayoutManager.VERTICAL, false)
        gridLayoutManager.spanSizeLookup = getSpanSizeLookUp()
        slave_list.apply {
            layoutManager = gridLayoutManager
            adapter = categoryLevelTwoAdapter
        }
    }

    private fun getSpanSizeLookUp(): GridLayoutManager.SpanSizeLookup {
        return object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return when (slave_list.adapter?.getItemViewType(position)) {
                    Constants.ProductHeaderView -> fullItemSpan
                    Constants.YangLagiHitsView -> halfItemSpan
                    Constants.ProductView -> oneThirdSpan
                    Constants.HeaderShimmer -> fullItemSpan
                    Constants.ProductShimmer -> oneThirdSpan
                    else -> fullItemSpan
                }
            }
        }
    }

    private fun addShimmerItems(childList: ArrayList<CategoryChildItem>) {
        // adding shimmer elements in recyclerview
        val headerItem = CategoryChildItem()
        headerItem.itemType = Constants.HeaderShimmer
        childList.add(headerItem)
        val productItem = CategoryChildItem()
        productItem.itemType = Constants.ProductShimmer
        for (i in 1..12) {
            childList.add(productItem)
        }
    }

    private fun initViewModel() {
        val viewModelProvider = ViewModelProviders.of(this, viewModelFactory)
        categoryLevelTwoViewModel = viewModelProvider.get(CategoryLevelTwoViewModel::class.java)
    }

    override fun onPause() {
        categoryLevelTwoAdapter.pushTrackingEvents()
        super.onPause()
    }
}

interface Listener {
    fun refreshView(id: String, categoryName: String, applink: String?)
}