package com.tokopedia.common.travel.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.common.travel.data.entity.TravelCrossSelling
import com.tokopedia.common.travel.databinding.WidgetTravelCrossSellingBinding
import com.tokopedia.common.travel.presentation.adapter.TravelCrossSellAdapter
import com.tokopedia.unifycomponents.BaseCustomView

/**
 * @author by jessica on 2019-10-02
 */

class TravelCrossSellWidget @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
    BaseCustomView(context, attrs, defStyleAttr) {

    lateinit var adapter: TravelCrossSellAdapter

    private var binding = WidgetTravelCrossSellingBinding.inflate(LayoutInflater.from(context), this, true)

    fun buildView(travelCrossSelling: TravelCrossSelling) {
        with(binding) {
            titleTv.text = travelCrossSelling.meta.title

            crossSellingRv.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = TravelCrossSellAdapter()
            adapter.setItem(travelCrossSelling.items)
            crossSellingRv.adapter = adapter
        }
    }

    fun setListener(listener: TravelCrossSellAdapter.OnItemClickListener) {
        adapter.listener = listener
    }
}
