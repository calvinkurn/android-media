package com.tokopedia.core.network.retrofit.utils;

/**
 * @author anggaprasetiyo on 10/13/16.
 */

@Deprecated
public interface ErrorNetMessage {
    String MESSAGE_ERROR_DEFAULT = "Terjadi kesalahan, ulangi beberapa saat lagi";
    String MESSAGE_ERROR_FORBIDDEN = "Akses ditolak, ulangi beberapa saat lagi";
    String MESSAGE_ERROR_SERVER = "Terjadi kesalahan pada server, ulangi beberapa saat lagi";
    String MESSAGE_ERROR_NULL_DATA = "Data kosong, ulangi beberapa saat lagi";
    String MESSAGE_ERROR_TIMEOUT = "Koneksi timeout, Mohon ulangi beberapa saat lagi";
    String MESSAGE_ERROR_NO_CONNECTION = "Tidak ada koneksi internet";
    String MESSAGE_ERROR_NO_CONNECTION_FULL = "Tidak ada koneksi internet, silahkan coba lagi";
}
