package com.tokopedia.shop_widget.operationalhour.view.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.shop.common.graphql.data.shopoperationalhourslist.ShopOperationalHour
import com.tokopedia.shop_widget.R
import com.tokopedia.shop_widget.databinding.ShopOperationalHoursListBottomsheetBinding
import com.tokopedia.shop_widget.operationalhour.di.component.DaggerShopWidgetComponent
import com.tokopedia.shop_widget.operationalhour.di.module.ShopWidgetModule
import com.tokopedia.shop_widget.operationalhour.view.adapter.ShopOperationalHoursListBottomsheetAdapter
import com.tokopedia.shop_widget.operationalhour.view.viewmodel.ShopOperationalHourBottomSheetViewModel
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.utils.lifecycle.autoClearedNullable
import javax.inject.Inject
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.shop_widget.operationalhour.view.adapter.ShopOperationalHoursListBottomsheetAdapter.Companion.MAX_END_TIME
import com.tokopedia.shop_widget.operationalhour.view.adapter.ShopOperationalHoursListBottomsheetAdapter.Companion.MIN_START_TIME
import com.tokopedia.shop_widget.operationalhour.view.adapter.ShopOperationalHoursListBottomsheetAdapter.Companion.MONDAY_NUMBER
import com.tokopedia.shop_widget.operationalhour.view.adapter.ShopOperationalHoursListBottomsheetAdapter.Companion.SUNDAY_NUMBER
import com.tokopedia.usecase.coroutines.Fail


/**
 * Created by Rafli Syam on 16/04/2021
 */
class ShopOperationalHoursListBottomSheet : BottomSheetUnify() {

    companion object {
        private val TAG = ShopOperationalHoursListBottomSheet::class.java.simpleName
        private val KEY_SHOP_ID = "shop_id"

        fun createInstance(shopId: String): ShopOperationalHoursListBottomSheet = ShopOperationalHoursListBottomSheet().apply {
            arguments = Bundle().apply {
                putString(KEY_SHOP_ID, shopId)
            }
        }
    }

    private var viewModel: ShopOperationalHourBottomSheetViewModel? = null
    private var rvOperationalHours: RecyclerView? = null
    private var rvOperationalHoursAdapter: ShopOperationalHoursListBottomsheetAdapter? = null
    private var viewBinding by autoClearedNullable<ShopOperationalHoursListBottomsheetBinding>()
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private var shopId: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initInjector()
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(ShopOperationalHourBottomSheetViewModel::class.java)
        getArgumentData(arguments)
    }

    private fun initInjector() {
        activity?.run {
            DaggerShopWidgetComponent
                .builder()
                .shopWidgetModule(ShopWidgetModule())
                .baseAppComponent((application as BaseMainApplication).baseAppComponent)
                .build()
                .inject(this@ShopOperationalHoursListBottomSheet)
        }
    }

    private fun getArgumentData(arguments: Bundle?) {
        arguments?.let{
            shopId = arguments.getString(KEY_SHOP_ID, "")
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setupBottomSheetChildView()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        getShopOperationalHourData()
        observeShopOperationalHourData()
    }

    private fun observeShopOperationalHourData() {
        viewModel?.shopOperationalHoursListData?.observe(viewLifecycleOwner, { result ->
            hideLoaderUnify()
            when(result){
                is Success -> {
                    showOperationalHourUi()
                    val opsHoursList = result.data.getShopOperationalHoursList?.data
                    opsHoursList?.let { hourList ->
                        if (hourList.isNotEmpty()) {
                            updateShopHoursDataSet(hourList)
                        } else { // This is the case when the seller is not yet setting his operational hours
                            val defaultOperationalHours: List<ShopOperationalHour> = getDefaultShopOperationalHours()
                            updateShopHoursDataSet(defaultOperationalHours)
                        }
                    }
                }
                is Fail -> {
                    val errorMessage = ErrorHandler.getErrorMessage(context, result.throwable)
                    showErrorToast(errorMessage)
                }
            }
        })
    }

    private fun getDefaultShopOperationalHours(): List<ShopOperationalHour> {
        val defaultOperationalHours: MutableList<ShopOperationalHour> = mutableListOf()
        for (i in MONDAY_NUMBER..SUNDAY_NUMBER) { // Loop from monday to sunday
            defaultOperationalHours.add(
                ShopOperationalHour(
                    day = i,
                    endTime = MAX_END_TIME,
                    startTime = MIN_START_TIME,
                    status = Int.ONE
                )
            )
        }
        return defaultOperationalHours
    }

    private fun showErrorToast(message: String) {
        viewBinding?.let{
            Toaster.build(
                it.container,
                message,
                Toaster.LENGTH_INDEFINITE,
                Toaster.TYPE_ERROR,
                getString(R.string.shop_widget_ops_hour_retry)
            ) {
                getShopOperationalHourData()
            }.show()
        }
    }

    private fun hideLoaderUnify() {
        viewBinding?.loaderUnify?.hide()
    }

    private fun showOperationalHourUi() {
        viewBinding?.icTitleOpsHourBottomsheet?.show()
        viewBinding?.tvOpsHourBottomsheet?.show()
        viewBinding?.rvShopOperationalHoursList?.show()
    }

    private fun getShopOperationalHourData() {
        viewModel?.getShopOperationalHoursList(shopId)
    }

    fun show(fragmentManager: FragmentManager?) {
        fragmentManager?.let {
            show(it, TAG)
        }
    }

    private fun updateShopHoursDataSet(newList: List<ShopOperationalHour>) {
        rvOperationalHoursAdapter?.addOperationalHourListData(newList.toMutableList())
    }

    private fun setupBottomSheetChildView() {
        viewBinding = ShopOperationalHoursListBottomsheetBinding.inflate(LayoutInflater.from(context)).apply {
            rvOperationalHours = this.rvShopOperationalHoursList
            setTitle(getString(R.string.shop_ops_hour_bottomsheet_title))
            setChild(this.root)
            setCloseClickListener {
                dismiss()
            }
        }
    }

    private fun initRecyclerView() {
        rvOperationalHoursAdapter = ShopOperationalHoursListBottomsheetAdapter(context)
        rvOperationalHours?.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = rvOperationalHoursAdapter
        }
    }
}
