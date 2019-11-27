package com.tokopedia.gamification.pdp.presentation.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.ViewFlipper
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.recyclerview.EndlessRecyclerViewScrollListener
import com.tokopedia.design.bottomsheet.CloseableBottomSheetDialog
import com.tokopedia.gamification.R
import com.tokopedia.gamification.pdp.data.di.components.DaggerPdpComponent
import com.tokopedia.gamification.pdp.data.di.scopes.GamificationPdpScope
import com.tokopedia.gamification.pdp.presentation.adapters.PdpGamificationAdapter
import com.tokopedia.gamification.pdp.presentation.adapters.PdpGamificationAdapterTypeFactory
import com.tokopedia.gamification.pdp.presentation.viewmodels.PdpDialogViewModel
import com.tokopedia.unifyprinciples.Typography

class PdpGamificationView  {

    init {
//        initViews()
    }

    private val CONTAINER_LOADING = 0
    private val CONTAINER_LIST = 1
    private val CONTAINER_ERROR = 2

    private val PAGE_NAME = "gamepage"

    private lateinit var tvTitle: Typography
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewFlipper: ViewFlipper

    private lateinit var adapter: PdpGamificationAdapter
    private lateinit var scrollListener: EndlessRecyclerViewScrollListener
    private val dataList = ArrayList<Visitable<Any>>()

    @GamificationPdpScope
    lateinit var viewModel: PdpDialogViewModel

    fun getLayout() = R.layout.dialog_pdp_gamification
    lateinit var context: Context


    private fun initViews(root: View) {
        injectComponents()

        recyclerView = root.findViewById(R.id.recyclerView)
        viewFlipper = root.findViewById(R.id.viewFlipper)
        tvTitle = root.findViewById(R.id.tvTitle)

        adapter = PdpGamificationAdapter(PdpGamificationAdapterTypeFactory(), dataList)


        setListeners()
        getRecommendationParams()
    }

    fun showDialog(context: Context) {
        val bottomSheet = CloseableBottomSheetDialog.createInstanceRounded(context)
        val v = LayoutInflater.from(context).inflate(getLayout(), null)
        bottomSheet.setContentView(v)
        bottomSheet.show()
        this.context = context
        initViews(v)
        return
    }


    private fun injectComponents() {
        DaggerPdpComponent.builder()
                .build().inject(this)
    }

    private fun setListeners() {
        val layoutManager = GridLayoutManager(context, 2)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter

        scrollListener = object:EndlessRecyclerViewScrollListener(layoutManager){
            override fun onLoadMore(page: Int, totalItemsCount: Int) {
                viewModel.getProducts()
            }

        }
        recyclerView.addOnScrollListener(scrollListener)

        viewModel.productLiveData.observe(context as AppCompatActivity, Observer {

        })

        viewModel.recommendationLiveData.observe(context as AppCompatActivity, Observer {
            //Do nothing
        })


    }

    protected fun getRecommendationParams() {
        viewModel.getRecommendationParams(PAGE_NAME)
    }


}