package com.tokopedia.entertainment.pdp.data.pdp.mapper

import com.tokopedia.entertainment.pdp.data.Outlet

object EventLocationMapper{
    fun getLatitude(outlet: Outlet): Double{
        val coordinates =  getCoordinate(outlet)
        return coordinates[0].toDouble()
    }

    fun getLongitude(outlet: Outlet): Double{
        val coordinates =  getCoordinate(outlet)
        return coordinates[1].toDouble()
    }

    fun getCoordinate(outlet: Outlet): List<String>{
        return outlet.coordinates.split(",")
    }
}