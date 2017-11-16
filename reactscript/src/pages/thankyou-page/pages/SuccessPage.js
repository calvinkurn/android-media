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
      <View style={{ alignItems: 'center', backgroundColor: 'white' }}>
        <Text style={{ fontSize: 17, marginTop: 30, fontWeight: 'bold', color: 'rgba(0,0,0,0.54)' }}>
          Pembayaran Berhasil
        </Text>

        <Text style={{ fontSize: 14, marginTop: 12, color: 'rgba(0,0,0,0.54)', fontWeight: 'bold' }}>
          Terimakasih telah bertransaksi
        </Text>
        <Text style={{ fontSize: 14, marginTop: 30, color: 'rgba(0,0,0,0.54)' }}>
          Pembayaran
        </Text>
        <Image source={{ uri: 'icon_no_data_grey' }} style={styles.mascot} />
        <Text style={{ fontSize: 14, marginTop: 5, color: 'red', fontWeight: 'bold' }}>
         {this.props.data.amount}
        </Text>
        <Text style={{ fontSize: 14, marginTop: 5, color: 'green', textDecorationLine: 'underline', }}>
          Lihat detail transaksi
        </Text>

        <Text style={{ fontSize: 14, marginTop: 5, color: 'rgba(0,0,0,0.54)' }}>
          Metode : {this.props.data.template}
        </Text>

        <TouchableOpacity>
          <View style={styles.buttonHolder}>
            <Text style={{ color: 'white', fontSize: 16 }}>Cek Status Pemesanan</Text>
          </View>
        </TouchableOpacity>
      </View>
    )
  }
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
})

export default SuccessScreen