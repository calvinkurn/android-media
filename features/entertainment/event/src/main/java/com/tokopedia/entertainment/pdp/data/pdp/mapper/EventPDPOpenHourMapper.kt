package com.tokopedia.entertainment.pdp.data.pdp.mapper

import com.tokopedia.entertainment.pdp.data.pdp.OpenHour

object EventPDPOpenHourMapper{
    fun openHourMapperTitle(openHour : String): String{
        return openHourMapperList(openHour).get(0)
    }

    fun openHourMapperList(openHour: String): List<String>{
        return openHour.split(",")
    }

    fun openHourList(openHour: String): List<OpenHour>{
        val listOpenHour : MutableList<OpenHour> = mutableListOf()
        val list = openHourMapperList(openHour)
        for(i in list.indices){
            if(i!=0){
                listOpenHour.add(splitOpenHour(list[i]))
            }
        }
        return listOpenHour
    }

    fun splitOpenHour(openHour : String): OpenHour{
        val splitHour = openHour.split("(")
        return if(splitHour.isNotEmpty())
         OpenHour(splitHour[0], splitHour[1].substringBefore(")"))
        else OpenHour("","")
    }
}