package com.tokopedia.profilecompletion.profileinfo.tracker

import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils

class ProfileInfoTracker {
    private val tracker = TrackApp.getInstance().gtm

    // tracker ID: 28680
    fun trackOnInfoProfileClick(label: String) {
	track(
	    TrackAppUtils.gtmData(
	    EVENT_CLICK_ACCOUNT,
	    CATEGORY_CHANGE_PROFILE,
	    ACTION_CLICK_INFORMATION,
	    label)
	)
    }

    // tracker ID: 28681
    fun trackOnClickBackBtn() {
	track(
	    TrackAppUtils.gtmData(
		EVENT_CLICK_ACCOUNT,
		CATEGORY_CHANGE_PROFILE,
		ACTION_CLICK_BACK,
		LABEL_CHANGE_PROFILE_PAGE)
	)
    }

    // tracker ID: 28682
    fun trackOnChangeProfilePictureClick() {
	track(
	    TrackAppUtils.gtmData(
		EVENT_CLICK_ACCOUNT,
		CATEGORY_CHANGE_PROFILE,
		ACTION_CLICK_UBAH_FOTO_PROFIL,
		"")
	)
    }

    // tracker ID: 28683 - Click
    fun trackOnEntryPointListClick(label: String) {
	track(
	    TrackAppUtils.gtmData(
		EVENT_CLICK_ACCOUNT,
		CATEGORY_CHANGE_PROFILE,
		ACTION_CLICK_ENTRY_POINT,
		"$LABEL_CLICK - $label")
	)
    }

    // tracker ID: 28683 - Success
    fun trackOnEntryPointListSuccess(label: String) {
	track(
	    TrackAppUtils.gtmData(
		EVENT_CLICK_ACCOUNT,
		CATEGORY_CHANGE_PROFILE,
		ACTION_CLICK_ENTRY_POINT,
		"$LABEL_SUCCESS - $label")
	)
    }

    // tracker ID: 28684 - Click
    fun trackOnClickBtnSimpanClick() {
	track(
	    TrackAppUtils.gtmData(
		EVENT_CLICK_ACCOUNT,
		CATEGORY_CHANGE_PROFILE,
		ACTION_CLICK_BTN_SIMPAN,
		LABEL_CLICK)
	)
    }

    // tracker ID: 28684 - Success
    fun trackOnClickBtnSimpanSuccess(label: String) {
	track(
	    TrackAppUtils.gtmData(
		EVENT_CLICK_ACCOUNT,
		CATEGORY_CHANGE_PROFILE,
		ACTION_CLICK_BTN_SIMPAN,
		LABEL_CLICK)
	)
    }

    // tracker ID: 28684 - Failed
    fun trackOnClickBtnSimpanFailed(errorMsg: String) {
	track(
	    TrackAppUtils.gtmData(
		EVENT_CLICK_ACCOUNT,
		CATEGORY_CHANGE_PROFILE,
		ACTION_CLICK_BTN_SIMPAN,
		"$LABEL_SUCCESS - $errorMsg")
	)
    }

    // tracker ID: 28685
    fun trackOnClickBtnBackChangeName() {
	track(
	    TrackAppUtils.gtmData(
		EVENT_CLICK_ACCOUNT,
		CATEGORY_CHANGE_NAME,
		ACTION_CLICK_BACK,
		"")
	)
    }

    // tracker ID: 28686
    fun trackOnCloseBottomSheetChangeName() {
	track(
	    TrackAppUtils.gtmData(
		EVENT_CLICK_ACCOUNT,
		CATEGORY_CHANGE_NAME,
		ACTION_CLOSE_BOTTOM_SHEET_CHANGE_NAME,
		"")
	)
    }

    // tracker ID: 28687 - Click
    fun trackOnBtnSimpanChangeBirthdayClick() {
	track(
	    TrackAppUtils.gtmData(
		EVENT_CLICK_ACCOUNT,
		CATEGORY_CHANGE_BIRTHDAY,
		ACTION_CLICK_BTN_SIMPAN,
		LABEL_CLICK)
	)
    }

    // tracker ID: 28687 - Success
    fun trackOnBtnSimpanChangeBirthdaySuccess() {
	track(
	    TrackAppUtils.gtmData(
		EVENT_CLICK_ACCOUNT,
		CATEGORY_CHANGE_BIRTHDAY,
		ACTION_CLICK_BTN_SIMPAN,
		LABEL_SUCCESS)
	)
    }

    // tracker ID: 28687 - Failed
    fun trackOnBtnSimpanChangeBirthdayFailed(errorMsg: String) {
	track(
	    TrackAppUtils.gtmData(
		EVENT_CLICK_ACCOUNT,
		CATEGORY_CHANGE_BIRTHDAY,
		ACTION_CLICK_BTN_SIMPAN,
		"$LABEL_FAILED - $errorMsg")
	)
    }

    // tracker ID: 28688
    fun trackOnCloseBottomSheetChangeBirthday() {
	track(
	    TrackAppUtils.gtmData(
		EVENT_CLICK_ACCOUNT,
		CATEGORY_CHANGE_BIRTHDAY,
		ACTION_CLOSE_BOTTOM_SHEET_CHANGE_BIRTHDAY,
		"")
	)
    }

    // tracker ID: 28689 - Click
    fun trackOnBtnSimpanChangeGenderClick() {
	track(
	    TrackAppUtils.gtmData(
		EVENT_CLICK_ACCOUNT,
		CATEGORY_CHANGE_GENDER,
		ACTION_CLICK_BTN_SIMPAN,
		LABEL_CLICK)
	)
    }

    // tracker ID: 28689 - Success
    fun trackOnBtnSimpanChangeGenderSuccess() {
	track(
	    TrackAppUtils.gtmData(
		EVENT_CLICK_ACCOUNT,
		CATEGORY_CHANGE_GENDER,
		ACTION_CLICK_BTN_SIMPAN,
		LABEL_SUCCESS)
	)
    }

    // tracker ID: 28689 - Failed
    fun trackOnBtnSimpanChangeGenderFailed(errorMsg: String) {
	track(
	    TrackAppUtils.gtmData(
		EVENT_CLICK_ACCOUNT,
		CATEGORY_CHANGE_GENDER,
		ACTION_CLICK_BTN_SIMPAN,
		"$LABEL_FAILED - $errorMsg")
	)
    }


    // tracker ID: 28691
    fun trackClickOnBtnBackAddEmail() {
	track(
	    TrackAppUtils.gtmData(
		EVENT_CLICK_ACCOUNT,
		CATEGORY_ADD_EMAIL,
		ACTION_CLICK_BACK,
		"")
	)
    }

    // tracker ID: 28693
    fun trackClickOnBtnBackChangeEmail() {
	track(
	    TrackAppUtils.gtmData(
		EVENT_CLICK_ACCOUNT,
		CATEGORY_CHANGE_EMAIL,
		ACTION_CLICK_BACK,
		"")
	)
    }

    // tracker ID: 28695
    fun trackClickOnBtnBackAddPhone() {
	track(
	    TrackAppUtils.gtmData(
		EVENT_CLICK_ACCOUNT,
		CATEGORY_ADD_PHONE,
		ACTION_CLICK_BACK,
		"")
	)
    }

    // tracker ID: 28697
    fun trackClickOnBtnBackChangePhone() {
	track(
	    TrackAppUtils.gtmData(
		EVENT_CLICK_ACCOUNT,
		CATEGORY_CHANGE_PHONE,
		ACTION_CLICK_BACK,
		"")
	)
    }

    // tracker ID: 28698 - Click
    fun trackOnBtnSimpanUsernameClick(username: String) {
	track(
	    TrackAppUtils.gtmData(
		EVENT_CLICK_ACCOUNT,
		CATEGORY_INFO_PROFILE,
		ACTION_CLICK_BTN_SIMPAN,
		"$LABEL_CLICK - $username")
	)
    }

    // tracker ID: 28698 - Success
    fun trackOnBtnSimpanUsernameSuccess(username: String) {
	track(
	    TrackAppUtils.gtmData(
		EVENT_CLICK_ACCOUNT,
		CATEGORY_INFO_PROFILE,
		ACTION_CLICK_BTN_SIMPAN,
		"$LABEL_SUCCESS - $username")
	)
    }

    // tracker ID: 28698 - Failed
    fun trackOnBtnSimpanUsernameFailed(username: String) {
	track(
	    TrackAppUtils.gtmData(
		EVENT_CLICK_ACCOUNT,
		CATEGORY_INFO_PROFILE,
		ACTION_CLICK_BTN_SIMPAN,
		"$LABEL_FAILED - $username")
	)
    }

    // tracker ID: 28699
    fun trackClickOnBtnBackUsername() {
	track(
	    TrackAppUtils.gtmData(
		EVENT_CLICK_ACCOUNT,
		CATEGORY_INFO_PROFILE,
		ACTION_CLICK_BACK_USERNAME,
		"")
	)
    }

    // tracker ID: 28700 - Click
    fun trackOnBtnLanjutChangeBioClick() {
	track(
	    TrackAppUtils.gtmData(
		EVENT_CLICK_ACCOUNT,
		CATEGORY_INFO_PROFILE,
		ACTION_CLICK_BTN_LANJUT,
		"$LABEL_CLICK - bio")
	)
    }

    // tracker ID: 28700 - Success
    fun trackOnBtnLanjutChangeBioSuccess() {
	track(
	    TrackAppUtils.gtmData(
		EVENT_CLICK_ACCOUNT,
		CATEGORY_INFO_PROFILE,
		ACTION_CLICK_BTN_LANJUT,
		"$LABEL_SUCCESS - bio")
	)
    }

    // tracker ID: 28700 - Failed
    fun trackOnBtnLanjutChangeBioFailed(errorMsg: String) {
	track(
	    TrackAppUtils.gtmData(
		EVENT_CLICK_ACCOUNT,
		CATEGORY_INFO_PROFILE,
		ACTION_CLICK_BTN_LANJUT,
		"$LABEL_FAILED - bio - $errorMsg")
	)
    }

    // tracker ID: 28701
    fun trackClickOnBtnBackChangeBio() {
	track(
	    TrackAppUtils.gtmData(
		EVENT_CLICK_ACCOUNT,
		CATEGORY_INFO_PROFILE,
		ACTION_CLICK_BACK_BIO,
		"")
	)
    }

    // tracker ID: 28702
    fun trackClickOnLihatHalaman() {
	track(
	    TrackAppUtils.gtmData(
		EVENT_CLICK_ACCOUNT,
		CATEGORY_INFO_PROFILE,
		ACTION_CLICK_LIHAT_HALAMAN,
		"")
	)
    }

    // tracker ID: 28703 - Click
    fun trackOnBtnLanjutAddEmailClick() {
	track(
	    TrackAppUtils.gtmData(
		EVENT_CLICK_ACCOUNT,
		CATEGORY_ADD_EMAIL,
		ACTION_CLICK_BTN_LANJUT,
		LABEL_CLICK)
	)
    }

    // tracker ID: 28703 - Success
    fun trackOnBtnLanjutAddEmailSuccess() {
	track(
	    TrackAppUtils.gtmData(
		EVENT_CLICK_ACCOUNT,
		CATEGORY_ADD_EMAIL,
		ACTION_CLICK_BTN_LANJUT,
		LABEL_SUCCESS)
	)
    }

    // tracker ID: 28703 - Failed
    fun trackOnBtnLanjutAddEmailFailed(errorMsg: String) {
	track(
	    TrackAppUtils.gtmData(
		EVENT_CLICK_ACCOUNT,
		CATEGORY_ADD_EMAIL,
		ACTION_CLICK_BTN_LANJUT,
		"$LABEL_FAILED - $errorMsg")
	)
    }

    private fun track(data: MutableMap<String, Any>) {
	data[BUSSINESS_UNIT] = BUSSINESS_UNIT
	data[CURRENT_SITE] = CURRENT_SITE
	tracker.sendGeneralEvent(data)
    }

    companion object {
        const val EVENT_CLICK_ACCOUNT = "clickAccount"
	const val CATEGORY_CHANGE_PROFILE = "account setting - change profile"
	const val CATEGORY_CHANGE_NAME = "account setting - change name"
	const val CATEGORY_CHANGE_BIRTHDAY = "account setting - change birthday"
	const val CATEGORY_CHANGE_GENDER = "account setting - change gender"
	const val CATEGORY_ADD_EMAIL = "account setting - add email"
	const val CATEGORY_CHANGE_EMAIL = "account setting - change email"
	const val CATEGORY_ADD_PHONE = "account setting - add phone"
	const val CATEGORY_CHANGE_PHONE = "account setting - change phone"
	const val CATEGORY_INFO_PROFILE = "account setting - info profile"


	const val ACTION_CLICK_INFORMATION = "click information"
	const val ACTION_CLICK_BACK = "click on button back"
	const val ACTION_CLICK_BACK_USERNAME = "click on button back username"
	const val ACTION_CLICK_BACK_BIO = "click on button back bio"
	const val ACTION_CLICK_BTN_LANJUT = "click on button lanjut"
	const val ACTION_CLICK_UBAH_FOTO_PROFIL = "click on buttoh ubah foto profil"
	const val ACTION_CLICK_ENTRY_POINT = "click on entry point"
	const val ACTION_CLICK_BTN_SIMPAN = "click on button simpan"
	const val ACTION_CLOSE_BOTTOM_SHEET_CHANGE_NAME = "close bottomsheet change name"
	const val ACTION_CLOSE_BOTTOM_SHEET_CHANGE_BIRTHDAY = "close bottomsheet change birthday"
	const val ACTION_CLICK_LIHAT_HALAMAN = "click on button lihat halaman"

	const val LABEL_CLICK = "click"
	const val LABEL_SUCCESS = "success"
	const val LABEL_FAILED = "failed"
	const val LABEL_CHANGE_PROFILE_PAGE = "change profile page"

	const val BUSSINESS_UNIT = "user platform"
	const val CURRENT_SITE = "tokopediamarketplace"
    }
}