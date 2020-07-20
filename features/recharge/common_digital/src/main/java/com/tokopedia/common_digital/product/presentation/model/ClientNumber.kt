package com.tokopedia.common_digital.product.presentation.model

import android.os.Parcel
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

import java.util.ArrayList

/**
 * @author anggaprasetiyo on 5/8/17.
 */
@Parcelize
class ClientNumber(val name: String? = null,
                   val type: String? = null,
                   val text: String? = null,
                   val placeholder: String? = null,
                   val _default: String? = null,
                   val validation: List<Validation> = ArrayList(),
                   val additionalButton: AdditionalButton? = null,
                   val isEmoney: Boolean? = false) : Parcelable
