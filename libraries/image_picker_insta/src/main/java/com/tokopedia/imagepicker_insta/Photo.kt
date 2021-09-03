package com.tokopedia.imagepicker_insta

import android.text.TextUtils
import com.tokopedia.imagepicker_insta.util.FileUtil

class Photo {
    companion object{
        fun validName(name: String): Boolean {
            if (!TextUtils.isEmpty(name) && !name.contains("ExternalShare")) {
                val extn = name.substring(name.lastIndexOf(".") + 1)
                return supportExtn(extn)
            }
            return false
        }

        fun supportExtn(extn: String?): Boolean {
            return ("jpg".equals(extn, ignoreCase = true)
                    || "png".equals(extn, ignoreCase = true)
                    || "jpeg".equals(extn, ignoreCase = true))
        }
    }
}