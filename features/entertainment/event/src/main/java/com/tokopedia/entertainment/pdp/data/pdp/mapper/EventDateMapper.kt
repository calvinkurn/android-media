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
        return productDetailData.maxEndDate
    }

    fun getStartDate(productDetailData: ProductDetailData): String {
        return productDetailData.saleStartDate
    }

    fun isMaxDateNotMoreThanSelected(productDetailData: ProductDetailData, selectedDate: String):Boolean{
        return Date(productDetailData.maxEndDate.toLong() * 1000).after(Date(selectedDate.toLong() * 1000))
    }

    fun isMinDateNotLessThanSelected(productDetailData: ProductDetailData, selectedDate: String): Boolean{
        return Date(productDetailData.saleStartDate.toLong() * 1000).before(Date(selectedDate.toLong() * 1000))
    }

    fun getActiveDate(dates: List<String>): List<Date>{
        val listActiveDate : MutableList<Date> = mutableListOf()
        for (date in dates){
            listActiveDate.add(Date(date.toLong() * 1000))
        }
        return listActiveDate.sortedBy {it}
    }

    fun checkDate(listActiveDate : List<String>, selectedDate: String): Boolean {
        for(date in listActiveDate){
            if (selectedDate.isEmpty() && listActiveDate.size == 1)
                return true
            else if(EventDateUtil.convertUnixToToday(date.toLong()) == EventDateUtil.convertUnixToToday(selectedDate.toLong())){
                return true
            }
        }
        return false
    }

    fun checkStartSale(startDateUnix:String, currentDate:Date):Boolean{
        val startDate = Date(startDateUnix.toLong() * 1000)
        return currentDate.compareTo(startDate) > 0
    }

    fun checkNotEndSale(endDateUnix:String, currentDate: Date):Boolean{
        val endDateUnix = Date(endDateUnix.toLong() * 1000)
        return endDateUnix.compareTo(currentDate) > 0
    }

    fun getDate(listActiveDate : List<String>, selectedDate: String): String {
        for(date in listActiveDate){
            if (selectedDate.isEmpty() && listActiveDate.size == 1)
                return date
            else if(EventDateUtil.convertUnixToToday(date.toLong()) == selectedDate.toLong()){
                return date
            }
        }
        return selectedDate
    }

}