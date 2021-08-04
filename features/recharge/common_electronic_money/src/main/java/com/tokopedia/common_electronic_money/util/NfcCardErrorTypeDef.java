package com.tokopedia.common_electronic_money.util;

import androidx.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@StringDef({
        NfcCardErrorTypeDef.CARD_NOT_FOUND,
        NfcCardErrorTypeDef.FAILED_READ_CARD,
        NfcCardErrorTypeDef.FAILED_UPDATE_BALANCE})
@Retention(RetentionPolicy.SOURCE)
public @interface NfcCardErrorTypeDef {
    String CARD_NOT_FOUND = "Hmm, kartu ini belum didukung";
    String FAILED_READ_CARD = "Maaf, cek saldo belum berhasil";
    String FAILED_UPDATE_BALANCE = "Pembaruan informasi saldo Gagal, silakan tempelkan ulang kartu Anda.";
    String FAILED_REFRESH_TOKEN = "Terjadi kesalahan, silakan coba beberapa saat lagi";
}
