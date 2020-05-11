package com.tokopedia.entertainment.pdp.data.pdp.mapper

import com.tokopedia.entertainment.pdp.data.ProductDetailData
import java.util.*

object EventDateMapper{

    fun getSizeSchedule(productDetailData: ProductDetailData):Boolean {
        return productDetailData.schedules.size > 1
    }

    fun getEndDate(productDetailData: ProductDetailData): String {
        return productDetailData.saleEndDate
    }

    fun getStartDate(productDetailData: ProductDetailData): String {
        if(productDetailData.schedules.isNotEmpty()){
            return productDetailData.schedules[0].schedule.startDate
        } else{
            return ""
        }
    }

    fun getActiveDate(productDetailData: ProductDetailData): List<Date>{
        var listActiveDate : MutableList<Date> = mutableListOf()
        val schedules = productDetailData.schedules
        for (schedule in schedules){
            listActiveDate.add(Date(schedule.schedule.startDate.toLong() * 1000))
        }

        return listActiveDate
    }

}