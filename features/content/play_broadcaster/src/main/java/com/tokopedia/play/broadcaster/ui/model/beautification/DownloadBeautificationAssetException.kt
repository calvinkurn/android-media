package com.tokopedia.play.broadcaster.ui.model.beautification

/**
 * Created By : Jonathan Darwin on March 15, 2023
 */
open class DownloadBeautificationAssetException(message: String?) : Exception(message)

class DownloadLicenseAssetException(message: String?) : DownloadBeautificationAssetException(message)
class DownloadModelAssetException(message: String?) : DownloadBeautificationAssetException(message)
class DownloadCustomFaceAssetException(message: String?) : DownloadBeautificationAssetException(message)
