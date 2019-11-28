package com.tokopedia.gamification.pdp.presentation.views

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.ViewFlipper
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.recyclerview.EndlessRecyclerViewScrollListener
import com.tokopedia.design.bottomsheet.CloseableBottomSheetDialog
import com.tokopedia.gamification.R
import com.tokopedia.gamification.pdp.data.LiveDataResult
import com.tokopedia.gamification.pdp.data.Recommendation
import com.tokopedia.gamification.pdp.data.di.components.DaggerPdpComponent
import com.tokopedia.gamification.pdp.data.di.modules.AppModule
import com.tokopedia.gamification.pdp.presentation.adapters.PdpGamificationAdapter
import com.tokopedia.gamification.pdp.presentation.viewmodels.PdpDialogViewModel
import com.tokopedia.unifyprinciples.Typography
import javax.inject.Inject

class PdpGamificationView {

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
    private val dataList = ArrayList<Visitable<*>>()

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var viewModel: PdpDialogViewModel

    fun getLayout() = R.layout.dialog_pdp_gamification
    lateinit var context: Context


    private fun initViews(root: View) {
        injectComponents()

        recyclerView = root.findViewById(R.id.recyclerView)
        viewFlipper = root.findViewById(R.id.viewFlipper)
        tvTitle = root.findViewById(R.id.tvTitle)

        setupRv()
        setListeners()
        tvTitle.postDelayed({
            getRecommendationParams()
        }, 5 * 1000L)

    }

    private fun setupRv() {
//        adapter = PdpGamificationAdapter(PdpGamificationAdapterTypeFactory(), dataList)
        adapter = PdpGamificationAdapter(dataList)
        val layoutManager = GridLayoutManager(context, 2)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter

        scrollListener = object : EndlessRecyclerViewScrollListener(layoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int) {
                viewModel.getProducts()
            }

        }
        recyclerView.addOnScrollListener(scrollListener)
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
        val component = DaggerPdpComponent.builder()
                .appModule(AppModule(context))
                .build()
        component.inject(this)


        if (context is AppCompatActivity) {
            val viewModelProvider = ViewModelProviders.of(context as AppCompatActivity, viewModelFactory)
            viewModel = viewModelProvider[PdpDialogViewModel::class.java]
        }
    }

    private fun setListeners() {

        viewModel.productLiveData.observe(context as AppCompatActivity, Observer {
            when(it.status){
                LiveDataResult.STATUS.SUCCESS-> {
                    if(it.data!=null) {
                        dataList.addAll(it.data)
                    }
                }
                LiveDataResult.STATUS.ERROR->{
                    //Do nothing
                }
            }
        })

        viewModel.recommendationLiveData.observe(context as AppCompatActivity, Observer {
            //Do nothing
        })

    }

    protected fun getRecommendationParams() {
        viewModel.getRecommendationParams(PAGE_NAME)
    }


}