package com.tokopedia.groupchat.chatroom.kotlin.view.viewmodel.chatroom

import android.os.Parcel
import android.os.Parcelable

import java.text.SimpleDateFormat
import java.util.ArrayList
import java.util.Locale

/**
 * @author by nisie on 3/26/18.
 */

class SprintSaleViewModel : Parcelable {

    var listProduct: ArrayList<SprintSaleProductViewModel>? = null
    var campaignName: String? = null
    private var startDate: Long = 0
    private var endDate: Long = 0
    var redirectUrl: String? = null
    var formattedStartDate: String? = null
        private set
    var formattedEndDate: String? = null
        private set
    var sprintSaleType: String? = null
    var campaignId: String? = null

    constructor(campaignId: String, listProduct: ArrayList<SprintSaleProductViewModel>,
                campaignName: String, startDate: Long, endDate: Long, redirectUrl: String, sprintSaleType: String) {
        val localeID = Locale("in", "ID")
        val sdfHour = SimpleDateFormat("HH:mm", localeID)
        this.campaignId = campaignId
        this.listProduct = listProduct
        this.campaignName = campaignName
        this.startDate = startDate
        this.endDate = endDate
        this.redirectUrl = redirectUrl
        this.formattedStartDate = sdfHour.format(this.startDate)
        this.formattedEndDate = sdfHour.format(this.endDate)
        this.sprintSaleType = sprintSaleType
    }


    protected constructor(`in`: Parcel) {
        listProduct = `in`.createTypedArrayList(SprintSaleProductViewModel.CREATOR)
        campaignName = `in`.readString()
        startDate = `in`.readLong()
        endDate = `in`.readLong()
        redirectUrl = `in`.readString()
        formattedStartDate = `in`.readString()
        formattedEndDate = `in`.readString()
        sprintSaleType = `in`.readString()
        campaignId = `in`.readString()
    }

    fun getStartDate(): Long {
        return startDate
    }

    fun getEndDate(): Long {
        return endDate
    }

    fun setStartDate(startDate: Long) {
        val localeID = Locale("in", "ID")
        val sdfHour = SimpleDateFormat("HH:mm", localeID)
        this.startDate = startDate
        this.formattedStartDate = sdfHour.format(startDate)
    }

    fun setEndDate(endDate: Long) {
        val localeID = Locale("in", "ID")
        val sdfHour = SimpleDateFormat("HH:mm", localeID)
        this.endDate = endDate
        this.formattedEndDate = sdfHour.format(endDate)
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeTypedList(listProduct)
        dest.writeString(campaignName)
        dest.writeLong(startDate)
        dest.writeLong(endDate)
        dest.writeString(redirectUrl)
        dest.writeString(formattedStartDate)
        dest.writeString(formattedEndDate)
        dest.writeString(sprintSaleType)
        dest.writeString(campaignId)
    }

    companion object {

        val TYPE_UPCOMING = "flashsale_upcoming"
        val TYPE_ACTIVE = "flashsale_start"
        val TYPE_FINISHED = "flashsale_end"

        @JvmField
        val CREATOR: Parcelable.Creator<SprintSaleViewModel> = object : Parcelable.Creator<SprintSaleViewModel> {
            override fun createFromParcel(`in`: Parcel): SprintSaleViewModel {
                return SprintSaleViewModel(`in`)
            }

            override fun newArray(size: Int): Array<SprintSaleViewModel?> {
                return arrayOfNulls(size)
            }
        }
    }
}
