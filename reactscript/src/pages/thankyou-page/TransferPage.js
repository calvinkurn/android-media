import React, { Component } from 'react'
import {
  StyleSheet,
  Image,
  View,
  Button,
  TouchableOpacity,
  Text,
} from 'react-native'

class SuccessScreen extends React.Component {
  render() {
    console.log(this.props.data)

    return (
      <View style={{ alignItems: 'center' }}>
        <Text
          style={{
            fontSize: 17,
            marginTop: 30,
            fontWeight: 'bold',
            color: 'rgba(0,0,0,0.54)',
          }}
        >
          {' '}
          Checkout Berhasil{' '}
        </Text>

        <Text
          style={{
            fontSize: 14,
            marginTop: 12,
            color: 'rgba(0,0,0,0.54)',
            fontWeight: 'bold',
          }}
        >
          Mohon Segera Selesaikan Pembayaran
        </Text>
        <Text
          style={{ fontSize: 14, marginTop: 30, color: 'rgba(0,0,0,0.54)' }}
        >
          Sisa Waktu Pembayaran Anda
        </Text>
        <Text
           style={{ fontSize: 30, marginTop: 15, fontWeight: 'bold', color: 'rgba(0,0,0,0.54)' }}
        >
        {this.props.data.hour} : {this.props.data.minute} : {this.props.data.second}
        </Text>
        <Text
         style={{ fontSize: 14, marginTop: 5, color: 'rgba(0,0,0,0.54)' }}
        >
            Jam : Menit : Detik
         </Text>
         <Text
          style={{ fontSize: 14, marginTop: 10, color: 'rgba(0,0,0,0.54)' }}
         >
            Selasa 26 September 2017 17:54
          </Text>
        <Text
          style={{
            fontSize: 14,
            marginTop: 40,
            color: 'rgba(0,0,0,0.54)',
            fontWeight: 'bold',
          }}
        >
          Jumlah Yang Harus Dibayar
        </Text>
        <Text
          style={{
            fontSize: 14,
            marginTop: 20,
            color: 'red',
            fontWeight: 'bold',
          }}
        >
         {this.props.data.amount}
        </Text>
        <View style={{ alignItems: 'center', backgroundColor: 'grey' }}>
        <Text
          style={{ fontSize: 14, marginTop: 10, marginBottom: 10, color: 'white' ,textAlign : 'center'}}
         >
            Transfer Sampai Tepat 3 Digit Terakhir
            Perbedaan Jumlah Pembayaran akan mempengaruhi lama proses verifikasi
          </Text>
        </View>
        <Text
          style={{
            fontSize: 14,
            marginTop: 5,
            color: 'green',
            textDecorationLine: 'underline',
          }}
        >
          Lihat detail transaksi
        </Text>

        <Image source={{uri : this.props.data.bank_logo}} style={styles.stretch} />

        <Text style={{ fontSize: 20, marginTop: 5, fontWeight: 'bold' }}>
          {this.props.data.bank_num}
        </Text>

        <TouchableOpacity

        >
          <View style={styles.buttonHolder}>
            <Text style={{ color: 'white', fontSize: 16 }}>
              Cek Status Pemesanan
            </Text>
          </View>
        </TouchableOpacity>

      </View>
    )
  }1
}

const styles = StyleSheet.create({
  mascot: {
    width: 50,
    height: 50,
    marginTop: 5,
  },
  buttonHolder: {
    borderRadius: 3,
    backgroundColor: '#42b549',
    marginTop: 12,
    paddingVertical: 12,
    paddingHorizontal: 20,
  },
  stretch: {
      resizeMode: "contain",
      width: 200,
      height: 50,
      marginTop: 50,
    },
})

export default SuccessScreen