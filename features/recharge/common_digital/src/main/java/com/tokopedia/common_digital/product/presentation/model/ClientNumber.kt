package com.tokopedia.common_digital.product.presentation.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

import java.util.ArrayList

/**
 * @author anggaprasetiyo on 5/8/17.
 */
@Parcelize
class ClientNumber(var name: String? = null,
                   var type: String? = null,
                   var text: String? = null,
                   var placeholder: String? = null,
                   var _default: String? = null,
                   var validation: List<Validation> = ArrayList(),
                   var additionalButton: AdditionalButton? = null,
                   var isEmoney: Boolean? = false) : Parcelable
