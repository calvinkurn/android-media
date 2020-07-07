package com.tokopedia.entertainment.pdp.data.pdp.mapper

import com.tokopedia.entertainment.pdp.common.util.EventDateUtil
import com.tokopedia.entertainment.pdp.data.ProductDetailData
import java.util.*

object EventDateMapper{

    fun isScheduleWithDatePicker(productDetailData: ProductDetailData):Boolean {
        return productDetailData.dates.size > 1
    }

    fun isScheduleWithoutDatePicker(productDetailData: ProductDetailData):Boolean {
        return productDetailData.dates.size == 1
    }

    fun getEndDate(productDetailData: ProductDetailData): String {
        return productDetailData.saleEndDate
    }

    fun getStartDate(productDetailData: ProductDetailData): String {
        return productDetailData.saleStartDate
    }

    fun getActiveDate(productDetailData: ProductDetailData): List<Date>{
        val listActiveDate : MutableList<Date> = mutableListOf()
        val dates = productDetailData.dates
        for (date in dates){
            listActiveDate.add(Date(date.toLong() * 1000))
        }

        return listActiveDate
    }

    fun checkDate(list : List<String>, selectedDate: String): Boolean {
        for(date in list){
            if (selectedDate.isEmpty() && list.size == 1)
                return true
            else if(EventDateUtil.convertUnixToToday(date.toLong()) == selectedDate.toLong()){
                return true
            }
        }
        return false
    }

    fun getDate(list : List<String>, selectedDate: String): String {
        for(date in list){
            if (selectedDate.isEmpty() && list.size == 1)
                return date
            else if(EventDateUtil.convertUnixToToday(date.toLong()) == selectedDate.toLong()){
                return date
            }
        }
        return selectedDate
    }

}