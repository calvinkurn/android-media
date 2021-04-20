package com.tokopedia.common.travel.presentation.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * @author by furqan on 03/11/2020
 */
@Parcelize
class TravelVideoBannerModel(
        var title: String = "",
        var imageUrl: String = "",
        var destinationLink: String = "",
        var id: String = "",
        var description: String = ""
) : Parcelable