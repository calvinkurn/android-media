package com.tokopedia.flight.bookingV3.data.mapper

import com.tokopedia.flight.common.constant.FlightErrorConstant

/**
 * @author by jessica on 2019-11-08
 */

class FlightBookingErrorCodeMapper {
    companion object {
        fun mapToFlightErrorCode(errorCode: Int): String {
            return when (errorCode) {
                56, 73, 83, 731, 732, 733, 734, 735 -> FlightErrorConstant.FLIGHT_SOLD_OUT
                1, 2, 3, 4, 8, 9, 13, 14, 15,
                16, 31, 36, 37, 42, 53, 78, 79,
                80, 84, 92, 96, 97, 99-> FlightErrorConstant.INVALID_JSON
                108 -> FlightErrorConstant.FAILED_ADD_FACILITY
                28 -> FlightErrorConstant.ERROR_PROMO_CODE
                86, 106, 107 -> FlightErrorConstant.FLIGHT_DUPLICATE_BOOKING
                44 -> FlightErrorConstant.FLIGHT_STILL_IN_PROCESS
                62 -> FlightErrorConstant.FLIGHT_DUPLICATE_USER_NAME
                22, 23, 98 -> FlightErrorConstant.FLIGHT_INVALID_USER
                else -> FlightErrorConstant.INVALID_JSON
            }
        }

        fun getErrorTitle(errorCode: String): String {
            return when (errorCode) {
                FlightErrorConstant.FLIGHT_SOLD_OUT -> "Yaah... Tiketnya habis"
                FlightErrorConstant.INVALID_JSON -> "Oops, ada sedikit gangguan"
                FlightErrorConstant.FAILED_ADD_FACILITY -> "Oops, nggak bisa tambah fasilitas"
                FlightErrorConstant.ERROR_PROMO_CODE -> "Kode promo ini nggak bisa dipakai"
                FlightErrorConstant.FLIGHT_DUPLICATE_BOOKING -> "Kamu sudah beli tiket ini sebelumnya"
                FlightErrorConstant.FLIGHT_STILL_IN_PROCESS -> "Pesananmu sebelumnya masih diproses"
                FlightErrorConstant.FLIGHT_DUPLICATE_USER_NAME -> "Ada nama penumpang yang sama, ubah dulu agar bisa bayar"
                FlightErrorConstant.FLIGHT_INVALID_USER -> "Tiket tidak bisa dipesan untuk nama ini"
                else -> "Oops, ada sedikit gangguan"
            }
        }

        fun getErrorSubtitle(errorCode: String): String {
            return when (errorCode) {
                FlightErrorConstant.FLIGHT_SOLD_OUT -> "Maaf, penerbangan yang kamu pilih nggak tersedia lagi. Kamu bisa cek penerbangan lain yang tak kalah oke."
                FlightErrorConstant.INVALID_JSON -> "Tenang, kami sedang memperbaiki gangguan ini. Tunggu beberapa saat untuk cari ulang tiketmu, ya. "
                FlightErrorConstant.FAILED_ADD_FACILITY -> "Bagasi & makanan hanya bisa ditambah maks. 12 jam sebelum keberangkatan. Lanjut bayar untuk pesan tiket tanpa tambah fasilitas."
                FlightErrorConstant.ERROR_PROMO_CODE -> "Coba cek kode promo lainnya dan pilih yang sesuai dengan pesananmu. "
                FlightErrorConstant.FLIGHT_DUPLICATE_BOOKING -> "Untuk pesan tiket dengan rute perjalanan yang sama, kamu bisa tunggu x menit. "
                FlightErrorConstant.FLIGHT_STILL_IN_PROCESS -> "Untuk pesan tiket dengan rute perjalanan yang sama, kamu bisa tunggu x menit"
                FlightErrorConstant.FLIGHT_INVALID_USER -> "Nama penumpang yang kamu tulis tidak diperkenankan oleh Tokopedia. Pastikan nama sudah benar, ya."
                else -> "Oops, ada sedikit gangguan"
            }
        }
    }
}