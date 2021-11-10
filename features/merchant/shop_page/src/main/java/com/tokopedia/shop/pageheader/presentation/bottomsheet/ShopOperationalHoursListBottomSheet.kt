package com.tokopedia.shop.pageheader.presentation.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.shop.R
import com.tokopedia.shop.common.graphql.data.shopoperationalhourslist.ShopOperationalHour
import com.tokopedia.shop.databinding.ShopOperationalHoursListBottomsheetBinding
import com.tokopedia.shop.pageheader.presentation.adapter.ShopOperationalHoursListBottomsheetAdapter
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.utils.lifecycle.autoClearedNullable

/**
 * Created by Rafli Syam on 16/04/2021
 */
class ShopOperationalHoursListBottomSheet : BottomSheetUnify() {

    companion object {
        @LayoutRes
        private val LAYOUT = R.layout.shop_operational_hours_list_bottomsheet
        private val TAG = ShopOperationalHoursListBottomSheet::class.java.simpleName

        fun createInstance(): ShopOperationalHoursListBottomSheet = ShopOperationalHoursListBottomSheet()
    }

    var operationalHourList = mutableListOf<ShopOperationalHour>()
    private var rvOperationalHours: RecyclerView? = null
    private var rvOperationalHoursAdapter: ShopOperationalHoursListBottomsheetAdapter? = null
    private var viewBinding by autoClearedNullable<ShopOperationalHoursListBottomsheetBinding>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setupBottomsheetChildView()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
    }

    fun show(fragmentManager: FragmentManager?) {
        fragmentManager?.let {
            show(it, TAG)
        }
    }

    fun updateShopHoursDataSet(newList: List<ShopOperationalHour>) {
        operationalHourList = newList.toMutableList()
    }

    private fun setupBottomsheetChildView() {
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
        rvOperationalHoursAdapter = ShopOperationalHoursListBottomsheetAdapter(context, operationalHourList)
        rvOperationalHours?.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = rvOperationalHoursAdapter
        }
    }
}
