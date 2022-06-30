package com.tokopedia.epharmacy.utils

sealed class EPharmacyUploadError
data class EPharmacyUploadEmptyImageError(val errMsg: String) : EPharmacyUploadError()
data class EPharmacyUploadNoPrescriptionIdError(val errMsg: String) : EPharmacyUploadError()
data class EPharmacyUploadBackendError(val errMsg: String) : EPharmacyUploadError()