package com.tokopedia.tkpd.network.retrofit.utils;

/**
 * @author anggaprasetiyo on 10/13/16.
 */
public interface ErrorNetMessage {
    String MESSAGE_ERROR_DEFAULT = "Terjadi kesalahan, ulangi beberapa saat lagi";
    String MESSAGE_ERROR_NULL_DATA = "Data kosong, ulangi beberapa saat lagi";
    String MESSAGE_ERROR_TIMEOUT = "Koneksi timeout, Mohon ulangi beberapa saat lagi";
    String MESSAGE_ERROR_NO_CONNECTION = "Tidak ada koneksi internet";

}
