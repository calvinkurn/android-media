package com.tokopedia.loginregister.common.analytics;

import android.content.Context;

import com.tokopedia.analytics.TrackAnalytics;
import com.tokopedia.analytics.firebase.FirebaseEvent;
import com.tokopedia.analytics.firebase.FirebaseParams;
import com.tokopedia.loginregister.LoginRegisterRouter;
import com.tokopedia.track.TrackApp;
import com.tokopedia.track.TrackAppUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author by ade on 12/04/18
 * https://docs.google.com/spreadsheets/d/1CBXovkdWu7NMkxrHIOJihMyfuRWNZvxgJd36KxLS25I/edit#gid=471355800
 */
public class RegisterAnalytics {

    public static final String SCREEN_REGISTER_INITIAL = "Register page";
    public static final String SCREEN_REGISTER_EMAIL = "Register with Email Page";

    private static final String EVENT_CLICK_LOGIN = "clickLogin";
    private static final String EVENT_CLICK_REGISTER = "clickRegister";
    private static final String EVENT_CLICK_ACTIVATION = "clickActivation";

    private static final String CATEGORY_LOGIN_PAGE = "login page";
    private static final String CATEGORY_REGISTER_PAGE = "register page";
    private static final String CATEGORY_REGISTER_WITH_EMAIL_PAGE = "register with email page";
    private static final String CATEGORY_REGISTER_WITH_PHONE_NUMBER_OTP =
            "register with phone number otp";
    private static final String CATEGORY_REGISTER_WITH_PHONE_NUMBER_PAGE =
            "register with phone number page";
    private static final String CATEGORY_EMAIL_AKTIVASI_AKUN = "" +
            "email aktivasi akun";
    private static final String CATEGORY_ACTIVATION_PAGE = "activation page";

    private static final String ACTION_CLICK_DAFTAR_TOP = "click daftar top";
    private static final String ACTION_CLICK_DAFTAR_BOTTOM = "click daftar bottom";
    private static final String ACTION_CLICK_ON_BUTTON_BACK = "click on button back";
    private static final String ACTION_CLICK_MASUK_TOP = "click masuk top";
    private static final String ACTION_CLICK_MASUK_BOTTOM = "click masuk bottom";
    private static final String ACTION_CLICK_ON_BUTTON_DAFTAR = "click on button daftar";
    private static final String ACTION_CLICK_ON_BUTTON_DAFTAR_EMAIL =
            "click on button daftar - email";
    private static final String ACTION_CLICK_ON_BUTTON_DAFTAR_PHONE_NUMBER =
            "click on button daftar - phone number";
    private static final String ACTION_CLICK_ON_BUTTON_GOOGLE = "click on button google";
    private static final String ACTION_CLICK_ON_BUTTON_FACEBOOK = "click on button facebook";
    private static final String ACTION_CLICK_YA_MASUK_TERDAFTAR_EMAIL =
            "click ya, masuk terdaftar - email";
    private static final String ACTION_CLICK_UBAH_TERDAFTAR_EMAIL = "click ubah terdaftar - email";
    private static final String ACTION_CLICK_UBAH_EMAIL = "click ubah - email";
    private static final String ACTION_CLICK_YA_BENAR_EMAIL =
            "click ya, benar - email";
    private static final String ACTION_CLICK_YA_BENAR_PHONE = "click ya, benar - phone number";
    private static final String ACTION_CLICK_UBAH_BENAR_PHONE = "click ubah, benar - phone number";
    private static final String ACTION_CLICK_YA_MASUK_TERDAFTAR_PHONE =
            "click ya, masuk terdaftar - phone number";
    private static final String ACTION_CLICK_UBAH_TERDAFTAR_PHONE =
            "click ubah terdaftar - phone number";
    private static final String ACTION_CLICK_ON_BUTTON_BACK_PHONE =
            "click ubah terdaftar - phone number";
    private static final String ACTION_CLICK_SYARAT_DAN_KETENTUAN = "click syarat dan ketentuan";
    private static final String ACTION_CLICK_KEBIJAKAN_PRIVASI = "click kebijakan privasi";
    private static final String ACTION_CLICK_ON_AKTIFKAN_AKUN_ANDA = "click on aktifkan akun anda";
    private static final String ACTION_CLICK_ON_BUTTON_AKTIVASI = "click on button aktivasi";
    private static final String ACTION_CLICK_KIRIM_ULANG = "click kirim ulang";
    private static final String ACTION_CLICK_OK_KIRIM_ULANG = "click ok (kirim ulang email)";
    private static final String ACTION_CLICK_UBAH_EMAIL_ACTIVATION = "click ubah email";
    private static final String ACTION_CLICK_ON_BUTTON_VERIFIKASI = "click on button verifikasi";
    private static final String ACTION_CLICK_ON_BUTTON_SELESAI = "click on button selesai";

    private static final String LABEL_EMPTY = "";
    private static final String LABEL_CLICK = "click";
    private static final String LABEL_SUCCESS = "success";
    private static final String LABEL_FAILED = "failed - ";
    private static final String LABEL_LOGIN_SUCCESS = "login success";
    private static final String LABEL_LOGIN_FAILED = "login failed - ";
    private static final String LABEL_REGISTER_SUCCESS = "register success";
    private static final String LABEL_REGISTER_FAILED = "register failed - ";
    public static final String LABEL_EMAIL_EXIST = "email exist";
    public static final String LABEL_PHONE_EXIST = "phone number exist";

    public static final String GOOGLE = "google";
    public static final String FACEBOOK = "facebook";

    public RegisterAnalytics(){ }

    //#R1
    public void trackClickTopSignUpButton(){
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_LOGIN,
                CATEGORY_LOGIN_PAGE,
                ACTION_CLICK_DAFTAR_TOP,
                LABEL_EMPTY
        ));
    }

    //#R2
    public void trackClickBottomSignUpButton(){
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_LOGIN,
                CATEGORY_LOGIN_PAGE,
                ACTION_CLICK_DAFTAR_BOTTOM,
                LABEL_EMPTY
        ));
    }

    //#R3
    public void trackClickOnBackButtonRegister(){
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_REGISTER,
                CATEGORY_REGISTER_PAGE,
                ACTION_CLICK_ON_BUTTON_BACK,
                LABEL_EMPTY
        ));
    }

    //#R4
    public void trackClickTopSignInButton(){
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_REGISTER,
                CATEGORY_REGISTER_PAGE,
                ACTION_CLICK_MASUK_TOP,
                LABEL_EMPTY
        ));
    }

    //#R5
    public void trackClickSignUpButton(){
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_REGISTER,
                CATEGORY_REGISTER_PAGE,
                ACTION_CLICK_ON_BUTTON_DAFTAR,
                LABEL_CLICK
        ));
    }

    //#R5
    public void trackFailedClickSignUpButton(String failedMessage){
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_REGISTER,
                CATEGORY_REGISTER_PAGE,
                ACTION_CLICK_ON_BUTTON_DAFTAR,
                LABEL_FAILED + failedMessage
        ));
    }

    //#R5
    public void trackClickEmailSignUpButton(){
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_REGISTER,
                CATEGORY_REGISTER_PAGE,
                ACTION_CLICK_ON_BUTTON_DAFTAR_EMAIL,
                LABEL_CLICK
        ));
    }

    //#R5
    public void trackSuccessClickEmailSignUpButton(){
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_REGISTER,
                CATEGORY_REGISTER_PAGE,
                ACTION_CLICK_ON_BUTTON_DAFTAR_EMAIL,
                LABEL_SUCCESS
        ));
    }

    //#R5
    public void trackFailedClickEmailSignUpButton(String failedMessage){
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_REGISTER,
                CATEGORY_REGISTER_PAGE,
                ACTION_CLICK_ON_BUTTON_DAFTAR_EMAIL,
                LABEL_FAILED + failedMessage
        ));
    }

    //#R5
    public void trackClickPhoneSignUpButton(){
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_REGISTER,
                CATEGORY_REGISTER_PAGE,
                ACTION_CLICK_ON_BUTTON_DAFTAR_PHONE_NUMBER,
                LABEL_CLICK
        ));
    }

    //#R5
    public void trackSuccessClickPhoneSignUpButton(){
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_REGISTER,
                CATEGORY_REGISTER_PAGE,
                ACTION_CLICK_ON_BUTTON_DAFTAR_PHONE_NUMBER,
                LABEL_SUCCESS
        ));
    }

    //#R5
    public void trackFailedClickPhoneSignUpButton(String failedMessage){
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_REGISTER,
                CATEGORY_REGISTER_PAGE,
                ACTION_CLICK_ON_BUTTON_DAFTAR_PHONE_NUMBER,
                LABEL_FAILED + failedMessage
        ));
    }

    //#R6
    public void trackClickGoogleButton(Context applicationContext){
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_REGISTER,
                CATEGORY_REGISTER_PAGE,
                ACTION_CLICK_ON_BUTTON_GOOGLE,
                LABEL_CLICK
        ));

        Map<String, Object> map = new HashMap<>();
        map.put(FirebaseParams.Home.LANDING_SCREEN_NAME, "GoogleSignInActivity");
        TrackAnalytics.sendEvent(FirebaseEvent.Home.LOGIN_PAGE_CLICK_LOGIN_GOOGLE,
                map, applicationContext);
    }

    //#R6
    public void trackSuccessClickLoginGoogleButton(){
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_REGISTER,
                CATEGORY_REGISTER_PAGE,
                ACTION_CLICK_ON_BUTTON_GOOGLE,
                LABEL_LOGIN_SUCCESS
        ));
    }

    //#R6
    public void trackFailedClickLoginGoogleButton(String loginFailedMessage){
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_REGISTER,
                CATEGORY_REGISTER_PAGE,
                ACTION_CLICK_ON_BUTTON_GOOGLE,
                LABEL_LOGIN_FAILED + loginFailedMessage
        ));
    }

    //#R6
    public void trackSuccessClickRegisterGoogleButton(){
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_REGISTER,
                CATEGORY_REGISTER_PAGE,
                ACTION_CLICK_ON_BUTTON_GOOGLE,
                LABEL_REGISTER_SUCCESS
        ));
    }

    //#R6
    public void trackFailedClickRegisterGoogleButton(String registerFailedMessage){
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_REGISTER,
                CATEGORY_REGISTER_PAGE,
                ACTION_CLICK_ON_BUTTON_GOOGLE,
                LABEL_REGISTER_FAILED + registerFailedMessage
        ));
    }

    //#R7
    public void trackClickFacebookButton(Context applicationContext){
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_REGISTER,
                CATEGORY_REGISTER_PAGE,
                ACTION_CLICK_ON_BUTTON_FACEBOOK,
                LABEL_CLICK
        ));

        Map<String, Object> map = new HashMap<>();
        map.put(FirebaseParams.Home.LANDING_SCREEN_NAME, "Facebook");
        TrackAnalytics.sendEvent(FirebaseEvent.Home.LOGIN_PAGE_CLICK_LOGIN_FACEBOOK,
                map, applicationContext);
    }

    //#R7
    public void trackSuccessClickLoginFacebookButton(){
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_REGISTER,
                CATEGORY_REGISTER_PAGE,
                ACTION_CLICK_ON_BUTTON_FACEBOOK,
                LABEL_LOGIN_SUCCESS
        ));
    }

    //#R7
    public void trackFailedClickLoginFacebookButton(String loginFailedMessage){
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_REGISTER,
                CATEGORY_REGISTER_PAGE,
                ACTION_CLICK_ON_BUTTON_FACEBOOK,
                LABEL_LOGIN_FAILED + loginFailedMessage
        ));
    }

    //#R7
    public void trackSuccessClickRegisterFacebookButton(){
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_REGISTER,
                CATEGORY_REGISTER_PAGE,
                ACTION_CLICK_ON_BUTTON_FACEBOOK,
                LABEL_REGISTER_SUCCESS
        ));
    }

    //#R7
    public void trackFailedClickRegisterFacebookButton(String registerFailedMessage){
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_REGISTER,
                CATEGORY_REGISTER_PAGE,
                ACTION_CLICK_ON_BUTTON_FACEBOOK,
                LABEL_REGISTER_FAILED + registerFailedMessage
        ));
    }

    //#R8
    public void trackClickBottomSignInButton(){
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_REGISTER,
                CATEGORY_REGISTER_PAGE,
                ACTION_CLICK_MASUK_BOTTOM,
                LABEL_EMPTY
        ));
    }

    //#R9
    public void trackClickYesButtonRegisteredEmailDialog(){
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_REGISTER,
                CATEGORY_REGISTER_PAGE,
                ACTION_CLICK_YA_MASUK_TERDAFTAR_EMAIL,
                LABEL_CLICK
        ));
    }

    //#R9
    public void trackSuccessClickYesButtonRegisteredEmailDialog(){
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_REGISTER,
                CATEGORY_REGISTER_PAGE,
                ACTION_CLICK_YA_MASUK_TERDAFTAR_EMAIL,
                LABEL_SUCCESS
        ));
    }

    //#R9
    public void trackFailedClickYesButtonRegisteredEmailDialog(String failedMessage){
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_REGISTER,
                CATEGORY_REGISTER_PAGE,
                ACTION_CLICK_YA_MASUK_TERDAFTAR_EMAIL,
                LABEL_FAILED + failedMessage
        ));
    }

    //#R10
    public void trackClickChangeButtonRegisteredEmailDialog(){
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_REGISTER,
                CATEGORY_REGISTER_PAGE,
                ACTION_CLICK_UBAH_TERDAFTAR_EMAIL,
                LABEL_EMPTY
        ));
    }

    //#R11
    public void trackClickYesButtonEmailDialog(){
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_REGISTER,
                CATEGORY_REGISTER_PAGE,
                ACTION_CLICK_YA_BENAR_EMAIL,
                LABEL_CLICK
        ));
    }

    //#R11
    public void trackSuccessClickYesButtonEmailDialog(){
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_REGISTER,
                CATEGORY_REGISTER_PAGE,
                ACTION_CLICK_YA_BENAR_EMAIL,
                LABEL_SUCCESS
        ));
    }

    //#R11
    public void trackFailedClickYesButtonEmailDialog(String failedMessage){
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_REGISTER,
                CATEGORY_REGISTER_PAGE,
                ACTION_CLICK_YA_BENAR_EMAIL,
                LABEL_FAILED + failedMessage
        ));
    }

    //#R12
    public void trackClickChangeButtonEmailDialog(){
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_REGISTER,
                CATEGORY_REGISTER_PAGE,
                ACTION_CLICK_UBAH_EMAIL,
                LABEL_EMPTY
        ));
    }

    //#R13
    public void trackClickOnBackButtonRegisterEmail(){
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_REGISTER,
                CATEGORY_REGISTER_WITH_EMAIL_PAGE,
                ACTION_CLICK_ON_BUTTON_BACK,
                LABEL_EMPTY
        ));
    }

    //#R14
    public void trackClickYesButtonPhoneDialog(){
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_REGISTER,
                CATEGORY_REGISTER_PAGE,
                ACTION_CLICK_YA_BENAR_PHONE,
                LABEL_CLICK
        ));
    }

    //#R14
    public void trackSuccessClickYesButtonPhoneDialog(){
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_REGISTER,
                CATEGORY_REGISTER_PAGE,
                ACTION_CLICK_YA_BENAR_PHONE,
                LABEL_SUCCESS
        ));
    }

    //#R14
    public void trackFailedClickYesButtonPhoneDialog(String failedMessage){
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_REGISTER,
                CATEGORY_REGISTER_PAGE,
                ACTION_CLICK_YA_BENAR_PHONE,
                LABEL_FAILED + failedMessage
        ));
    }

    //#R15
    public void trackClickChangeButtonPhoneDialog(){
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_REGISTER,
                CATEGORY_REGISTER_PAGE,
                ACTION_CLICK_UBAH_BENAR_PHONE,
                LABEL_EMPTY
        ));
    }

    //#R16
    public void trackClickYesButtonRegisteredPhoneDialog(){
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_REGISTER,
                CATEGORY_REGISTER_PAGE,
                ACTION_CLICK_YA_MASUK_TERDAFTAR_PHONE,
                LABEL_CLICK
        ));
    }

    //#R16
    public void trackSuccessClickYesButtonRegisteredPhoneDialog(){
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_REGISTER,
                CATEGORY_REGISTER_PAGE,
                ACTION_CLICK_YA_MASUK_TERDAFTAR_PHONE,
                LABEL_SUCCESS
        ));
    }

    //#R16
    public void trackFailedClickYesButtonRegisteredPhoneDialog(String failedMessage){
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_REGISTER,
                CATEGORY_REGISTER_PAGE,
                ACTION_CLICK_YA_MASUK_TERDAFTAR_PHONE,
                LABEL_FAILED + failedMessage
        ));
    }

    //#R16
    public void trackClickChangeButtonRegisteredPhoneDialog(){
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_REGISTER,
                CATEGORY_REGISTER_PAGE,
                ACTION_CLICK_UBAH_TERDAFTAR_PHONE,
                LABEL_EMPTY
        ));
    }


    //#R19
    public void trackClickSignUpButtonEmail(){
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_REGISTER,
                CATEGORY_REGISTER_WITH_EMAIL_PAGE,
                ACTION_CLICK_ON_BUTTON_DAFTAR,
                LABEL_CLICK
        ));
    }

    //#R19
    public void trackSuccessClickSignUpButtonEmail(){
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_REGISTER,
                CATEGORY_REGISTER_WITH_EMAIL_PAGE,
                ACTION_CLICK_ON_BUTTON_DAFTAR,
                LABEL_SUCCESS
        ));
    }

    //#R19
    public void trackFailedClickSignUpButtonEmail(String failedMessage){
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_REGISTER,
                CATEGORY_REGISTER_WITH_EMAIL_PAGE,
                ACTION_CLICK_ON_BUTTON_DAFTAR,
                LABEL_FAILED + failedMessage
        ));
    }

    //#R20
    public void trackClickTermConditionButton(){
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_REGISTER,
                CATEGORY_REGISTER_WITH_EMAIL_PAGE,
                ACTION_CLICK_SYARAT_DAN_KETENTUAN,
                LABEL_EMPTY
        ));
    }

    //#R21
    public void trackClickPrivacyPolicyButton(){
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_REGISTER,
                CATEGORY_REGISTER_WITH_EMAIL_PAGE,
                ACTION_CLICK_KEBIJAKAN_PRIVASI,
                LABEL_EMPTY
        ));
    }

    //#R22
    public void trackClickRegisterActivationLinkButton(){
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_ACTIVATION,
                CATEGORY_EMAIL_AKTIVASI_AKUN,
                ACTION_CLICK_ON_AKTIFKAN_AKUN_ANDA,
                LABEL_EMPTY
        ));
    }

    //#R22
    public void trackSuccessClickRegisterActivationLinkButton(){
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_ACTIVATION,
                CATEGORY_EMAIL_AKTIVASI_AKUN,
                ACTION_CLICK_ON_AKTIFKAN_AKUN_ANDA,
                LABEL_SUCCESS
        ));
    }

    //#R22
    public void trackFailedClickRegisterActivationLinkButton(String failedMessage){
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_ACTIVATION,
                CATEGORY_EMAIL_AKTIVASI_AKUN,
                ACTION_CLICK_ON_AKTIFKAN_AKUN_ANDA,
                LABEL_FAILED + failedMessage
        ));
    }

    //#R23
    public void trackClickOnBackButtonActivation(){
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_ACTIVATION,
                CATEGORY_ACTIVATION_PAGE,
                ACTION_CLICK_ON_BUTTON_BACK,
                LABEL_EMPTY
        ));
    }

    //#R24
    public void trackClickActivationButton(){
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_ACTIVATION,
                CATEGORY_ACTIVATION_PAGE,
                ACTION_CLICK_ON_BUTTON_AKTIVASI,
                LABEL_CLICK
        ));
    }

    //#R24
    public void trackSuccessClickActivationButton(){
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_ACTIVATION,
                CATEGORY_ACTIVATION_PAGE,
                ACTION_CLICK_ON_BUTTON_AKTIVASI,
                LABEL_SUCCESS
        ));
    }

    //#R24
    public void trackFailedClickActivationButton(String failedMessage){
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_ACTIVATION,
                CATEGORY_ACTIVATION_PAGE,
                ACTION_CLICK_ON_BUTTON_AKTIVASI,
                LABEL_FAILED + failedMessage
        ));
    }

    //#R25
    public void trackClickResendButton(){
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_ACTIVATION,
                CATEGORY_ACTIVATION_PAGE,
                ACTION_CLICK_KIRIM_ULANG,
                LABEL_CLICK
        ));
    }

    //#R25
    public void trackSuccessClickResendButton(){
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_ACTIVATION,
                CATEGORY_ACTIVATION_PAGE,
                ACTION_CLICK_KIRIM_ULANG,
                LABEL_SUCCESS
        ));
    }

    //#R25
    public void trackFailedClickResendButton(String failedMessage){
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_ACTIVATION,
                CATEGORY_ACTIVATION_PAGE,
                ACTION_CLICK_KIRIM_ULANG,
                LABEL_SUCCESS + failedMessage
        ));
    }

    //#R26
    public void trackClickOkResendButton(){
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_ACTIVATION,
                CATEGORY_ACTIVATION_PAGE,
                ACTION_CLICK_OK_KIRIM_ULANG,
                LABEL_CLICK
        ));
    }

    //#R26
    public void trackSuccessClickOkResendButton(){
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_ACTIVATION,
                CATEGORY_ACTIVATION_PAGE,
                ACTION_CLICK_OK_KIRIM_ULANG,
                LABEL_SUCCESS
        ));
    }

    //#R26
    public void trackFailedClickOkResendButton(String failedMessage){
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_ACTIVATION,
                CATEGORY_ACTIVATION_PAGE,
                ACTION_CLICK_OK_KIRIM_ULANG,
                LABEL_FAILED + failedMessage
        ));
    }

    //#R27
    public void trackClickChangeEmail(){
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_ACTIVATION,
                CATEGORY_ACTIVATION_PAGE,
                ACTION_CLICK_UBAH_EMAIL_ACTIVATION,
                LABEL_EMPTY
        ));
    }

    //#R28
    public void trackClickVerificationButton(){
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_REGISTER,
                CATEGORY_REGISTER_WITH_PHONE_NUMBER_OTP,
                ACTION_CLICK_ON_BUTTON_VERIFIKASI,
                LABEL_CLICK
        ));
    }

    //#R28
    public void trackSuccessClickVerificationButton(){
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_REGISTER,
                CATEGORY_REGISTER_WITH_PHONE_NUMBER_OTP,
                ACTION_CLICK_ON_BUTTON_VERIFIKASI,
                LABEL_SUCCESS
        ));
    }

    //#R28
    public void trackFailedClickVerificationButton(String failedMessage){
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_REGISTER,
                CATEGORY_REGISTER_WITH_PHONE_NUMBER_OTP,
                ACTION_CLICK_ON_BUTTON_VERIFIKASI,
                LABEL_FAILED + failedMessage
        ));
    }

    //#R29
    public void trackClickResendPhoneOtpButton(){
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_REGISTER,
                CATEGORY_REGISTER_WITH_PHONE_NUMBER_OTP,
                ACTION_CLICK_KIRIM_ULANG,
                LABEL_CLICK
        ));
    }

    //#R29
    public void trackSuccessClickResendPhoneOtpButton(){
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_REGISTER,
                CATEGORY_REGISTER_WITH_PHONE_NUMBER_OTP,
                ACTION_CLICK_KIRIM_ULANG,
                LABEL_SUCCESS
        ));
    }

    //#R29
    public void trackFailedClickResendPhoneOtpButton(String failedMessage){
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_REGISTER,
                CATEGORY_REGISTER_WITH_PHONE_NUMBER_OTP,
                ACTION_CLICK_KIRIM_ULANG,
                LABEL_FAILED + failedMessage
        ));
    }

    //#R30
    public void trackClickOnBackButtonPhoneNumber(){
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_REGISTER,
                CATEGORY_REGISTER_WITH_PHONE_NUMBER_PAGE,
                ACTION_CLICK_ON_BUTTON_BACK,
                LABEL_EMPTY
        ));
    }

    //#R31
    public void trackClickFinishButton(){
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_REGISTER,
                CATEGORY_REGISTER_WITH_PHONE_NUMBER_PAGE,
                ACTION_CLICK_ON_BUTTON_SELESAI,
                LABEL_CLICK
        ));
    }

    //#R31
    public void trackSuccessClickFinishButton(){
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_REGISTER,
                CATEGORY_REGISTER_WITH_PHONE_NUMBER_PAGE,
                ACTION_CLICK_ON_BUTTON_SELESAI,
                LABEL_SUCCESS
        ));
    }

    //#R31
    public void trackFailedClickFinishButton(String failedMessage){
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_REGISTER,
                CATEGORY_REGISTER_WITH_PHONE_NUMBER_PAGE,
                ACTION_CLICK_ON_BUTTON_SELESAI,
                LABEL_FAILED + failedMessage
        ));
    }

    //#R32
    public void trackClickTermConditionButtonPhoneNumber(){
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_REGISTER,
                CATEGORY_REGISTER_WITH_PHONE_NUMBER_PAGE,
                ACTION_CLICK_SYARAT_DAN_KETENTUAN,
                LABEL_EMPTY
        ));
    }

    //#R33
    public void trackClickPrivacyPolicyButtonPhoneNumber(){
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_REGISTER,
                CATEGORY_REGISTER_WITH_PHONE_NUMBER_PAGE,
                ACTION_CLICK_KEBIJAKAN_PRIVASI,
                LABEL_EMPTY
        ));
    }
}
