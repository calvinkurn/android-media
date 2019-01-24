package com.tokopedia.core.network.retrofit.utils;

/**
 * @author anggaprasetiyo on 10/13/16.
 */
public interface ErrorNetMessage {
    String MESSAGE_ERROR_DEFAULT = "Terjadi kesalahan, ulangi beberapa saat lagi";
    String MESSAGE_ERROR_FORBIDDEN = "Akses ditolak, ulangi beberapa saat lagi";
    String MESSAGE_ERROR_SERVER = "Terjadi kesalahan pada server, ulangi beberapa saat lagi";
    String MESSAGE_ERROR_NULL_DATA = "Data kosong, ulangi beberapa saat lagi";
    String MESSAGE_ERROR_TIMEOUT = "Koneksi timeout, Mohon ulangi beberapa saat lagi";
    String MESSAGE_ERROR_NO_CONNECTION = "Tidak ada koneksi internet";
    String MESSAGE_ERROR_NO_CONNECTION_FULL = "Tidak ada koneksi internet, silahkan coba lagi";


    String MESSAGE_ERROR_DEFAULT_SHORT = "Terjadi kesalahan";
    String MESSAGE_ERROR_FORBIDDEN_SHORT = "Akses ditolak";
    String MESSAGE_ERROR_SERVER_SHORT = "Terjadi kesalahan pada server";
    String MESSAGE_ERROR_NULL_DATA_SHORT = "Data kosong";
    String MESSAGE_ERROR_TIMEOUT_SHORT = "Koneksi timeout";
    String MESSAGE_ERROR_NO_CONNECTION_SHORT = "Terjadi kesalahan koneksi";
}
