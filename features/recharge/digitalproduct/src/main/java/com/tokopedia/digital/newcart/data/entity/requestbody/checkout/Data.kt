package com.tokopedia.digital.newcart.data.entity.requestbody.checkout

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author anggaprasetiyo on 3/9/17.
 */

class Data(@field:SerializedName("type")
           @field:Expose
           var type: String?, @field:SerializedName("id")
           @field:Expose
           var id: String?) {

}
