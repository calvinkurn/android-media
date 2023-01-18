package com.tokopedia.topads.view.customviews

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.topads.view.adapter.adstat.AdStatAdapter
import com.tokopedia.topads.view.adapter.adstat.AdStatHorizontalDecoration
import com.tokopedia.topads.view.datamodel.AdStatModel
import com.tokopedia.unifycomponents.BaseCustomView

class AdStatsView @JvmOverloads constructor(
    context: Context,
    attrs:AttributeSet?=null,
    defStyleAttr:Int = 0
) : BaseCustomView(context, attrs,defStyleAttr) {

    private var rv:RecyclerView?=null
    private var statAdapter:AdStatAdapter?=null

    init {
      inflateRecyclerView(context)
    }

    private fun inflateRecyclerView(context: Context){
        rv = RecyclerView(context).apply {
            layoutParams = LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT)
            val rvLayoutManager = LinearLayoutManager(
                context,
                LinearLayoutManager.HORIZONTAL,
                false
            )
            layoutManager = rvLayoutManager
            addItemDecoration(AdStatHorizontalDecoration(30))
            statAdapter = AdStatAdapter()
            adapter = statAdapter
        }
        addView(rv)
//        submitStatList(getDummyList())
    }

    private fun getDummyList() = listOf(
        AdStatModel("0","Tampil"),
        AdStatModel("1","Klik"),
        AdStatModel("0","Terjual")
    )

    fun submitStatList(list:List<AdStatModel>){
        statAdapter?.submitList(list)
    }

    fun setAllLoading(count:Int){
        val loadingList:MutableList<AdStatModel> = mutableListOf()
        for(i in 0 until count)
            loadingList.add(AdStatModel(loading = true))
        submitStatList(loadingList)
    }

    override fun onInterceptTouchEvent(ev: MotionEvent?) = true

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        event?.let {
            return when(it.action){
                MotionEvent.ACTION_DOWN ->{
                    performClick()
                    true
                }
                else -> false
            }
        }
        return false
    }

}
