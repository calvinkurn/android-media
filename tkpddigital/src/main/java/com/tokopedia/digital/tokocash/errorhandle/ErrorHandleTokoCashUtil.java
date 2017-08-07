package com.tokopedia.digital.tokocash.errorhandle;

/**
 * Created by nabillasabbaha on 7/27/17.
 */

public class ErrorHandleTokoCashUtil {

    public static String ERROR_INVALID_OTP = "ErrorInvalidOTP";
    public static String ERROR_REGISTER_WALLET = "ErrorRegisterWallet";
    public static String ERROR_VERIFY_PHONE_NUMBER = "ErrorVerifyPhoneNumber";
    public static String ERROR_WALLET_LINK = "ErrorWalletlink";

    public static String MESSAGE_DEFAULT_ACTIVATE_TOKOCASH = "Gagal melakukan aktivasi TokoCash, silakan coba beberapa saat lagi";
    public static String MESSAGE_ERROR_REQUEST_OTP = "Terjadi kesalahan, silakan coba beberapa saat lagi";
    public static String MESSAGE_INVALID_OTP = "Kode verifikasi yang anda masukkan salah";

    public static String handleError(String errorMessage) {
        if (ERROR_INVALID_OTP.equals(errorMessage)) {
            return MESSAGE_INVALID_OTP;
        } else if (ERROR_REGISTER_WALLET.equals(errorMessage) ||
                ERROR_VERIFY_PHONE_NUMBER.equals(errorMessage) ||
                ERROR_WALLET_LINK.equals(errorMessage)) {
            return MESSAGE_DEFAULT_ACTIVATE_TOKOCASH;
        } else {
            return MESSAGE_ERROR_REQUEST_OTP;
        }
    }
}
