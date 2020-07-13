package com.tokopedia.entertainment.pdp.data.pdp.mapper

import com.tokopedia.entertainment.pdp.data.Outlet

object EventLocationMapper{
    fun getLatitude(outlet: Outlet): Double{
        val coordinates =  getCoordinate(outlet)
        return if (coordinates.isNotEmpty())
             coordinates[0].toDouble()
        else 0.0
    }

    fun getLongitude(outlet: Outlet): Double{
        val coordinates =  getCoordinate(outlet)
        return if (coordinates.isNotEmpty())
         coordinates[1].toDouble()
        else 0.0
    }

    fun getCoordinate(outlet: Outlet): List<String>{
        return outlet.coordinates.split(",")
    }
}