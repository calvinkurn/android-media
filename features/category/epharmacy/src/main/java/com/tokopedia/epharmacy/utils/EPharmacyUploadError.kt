package com.tokopedia.epharmacy.utils

sealed class EPharmacyUploadError
data class EPharmacyUploadEmptyImageError(val showErrorToast: Boolean) : EPharmacyUploadError()
data class EPharmacyUploadNoPrescriptionIdError(val showErrorToast: Boolean) : EPharmacyUploadError()
data class EPharmacyUploadBackendError(val errMsg: String) : EPharmacyUploadError()
data class EPharmacyNoInternetError(val showErrorToast: Boolean) : EPharmacyUploadError()
data class EPharmacyMiniConsultationToaster(val showErrorToast: Boolean, val message: String, val ePharmacyGroupId: String?) : EPharmacyUploadError()
