package com.tokopedia.discovery2

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore


class Utils {

    companion object {
        const val TIME_ZONE = "Asia/Jakarta"
        const val TIMER_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm"
        const val TIMER_SPRINT_SALE_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss"
        const val DEFAULT_BANNER_WIDTH = 800
        const val DEFAULT_BANNER_HEIGHT = 150
        const val BANNER_SUBSCRIPTION_DEFAULT_STATUS = -1
        const val SEARCH_DEEPLINK = "tokopedia://search-autocomplete"
        const val SERIBU = 1000
        const val SEJUTA = 1000000
        const val SEMILIAR = 1000000000
        const val VIEW_LIMIT = 0.1


        fun extractDimension(url: String?, dimension: String = "height"): Int? {
            val uri = Uri.parse(url)
            return uri?.getQueryParameter(dimension)?.toInt()
        }

        fun shareData(context: Context?, shareTxt: String?, productUri: String?, image: Bitmap?) {
            val share = Intent(Intent.ACTION_SEND)
            share.type = "image/*"
            if (image != null) {
                share.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                val path: String = MediaStore.Images.Media.insertImage(context?.contentResolver, image, "Image Description", null)
                val uri = Uri.parse(path)
                share.putExtra(Intent.EXTRA_STREAM, uri)
            }
            share.putExtra(Intent.EXTRA_TEXT, shareTxt + "\n" + productUri)
            context?.startActivity(Intent.createChooser(share, shareTxt))
        }

        fun getCountView(countView: Double, notifyMeText: String = ""): String {
            if (countView >= SERIBU && countView < SEJUTA) {
                val rbCount = countView / SERIBU
                return if (checkForPrice(rbCount)) {
                    "${rbCount.toInt()} rb orang $notifyMeText"
                } else {
                    "${"%.1f".format(rbCount).replace('.', ',')} rb orang $notifyMeText"
                }
            } else if (countView >= SEJUTA && countView < SEMILIAR) {
                val jtCount = countView / SEJUTA
                return if (checkForPrice(jtCount)) {
                    "${jtCount.toInt()} jt orang $notifyMeText"
                } else {
                    "${"%.1f".format(jtCount).replace('.', ',')} jt orang $notifyMeText"
                }
            } else if (countView >= SEMILIAR) {
                val MCount = countView / SEMILIAR
                return if (checkForPrice(MCount)) {
                    "${MCount.toInt()} M orang $notifyMeText"
                } else {
                    "${"%.1f".format(MCount).replace('.', ',')} M orang $notifyMeText"
                }
            }
            return ""
        }

        private fun checkForPrice(currentViewCount: Double): Boolean {
            return currentViewCount % 1 < VIEW_LIMIT
        }
    }
}