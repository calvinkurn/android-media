import React, { Component } from 'react'
import {
  StyleSheet,
  Image,
  View,
  Button,
  TouchableOpacity,
  Text,
} from 'react-native'
import { connect } from 'react-redux'
import { NetworkModule } from 'NativeModules'
import { fetchDataDigital } from '../actions'
import { icons } from '../lib/icons'


class SuccessScreen extends React.Component {
    componentDidMount(){
        const { order_id } = this.props.data
        this.props.dispatch(fetchDataDigital(order_id))
    }
 

  render() {
      console.log(this.props)
    return (
      <View style={{ flex: 1, backgroundColor: '#ffffff' }}>
        <View style={{ alignItems: 'center', marginTop: 35 }}>
            <Text style={{ fontSize: 14, color: 'rgba(0, 0, 0, 0.7)', fontWeight: '600' }}>Segera selesaikan pembayaran Anda</Text>
            <Text style={{ fontSize: 13, marginTop: 10, color: 'rgba(0,0,0,0.54)' }}>Sisa waktu pembayaran</Text>
            <Text style={{ fontSize: 28, marginTop: 10, fontWeight: '500', color: 'rgba(0, 0, 0, 0.7)' }}>
                47 : 59 : 15
            </Text>
            <Text style={{ fontSize: 14, marginTop: 5, color: 'rgba(0,0,0,0.54)' }}>
                Jam : Menit : Detik
            </Text>
            <View style={{ flexDirection: 'row', marginTop: 10, marginBottom: 25 }}>
                <Text style={{ fontSize: 14, color: 'rgba(0,0,0,0.54)' }}>sebelum tanggal&nbsp;</Text>
                <Text style={{ fontSize: 14, color: 'rgba(0,0,0,0.54)', fontWeight: 'bold' }}>{this.props.data.deadline}</Text>
            </View>
            <View style={{ borderBottomColor: '#f1f1f1', borderBottomWidth: 1, width: '90%' }} />
        </View>
        

        <View>
            <View style={{ flexDirection: 'row', justifyContent: 'space-around', marginTop: 30 }}>
                <Text style={{ fontSize: 14, color: 'rgba(0,0,0,0.7)', fontWeight: 'bold' }}>Jumlah harus dibayar</Text>
                <Text style={{ fontSize: 18, color: 'red', fontWeight: 'bold', marginTop: -5 }}>{this.props.data.amount}</Text>
            </View>
            <View style={{ backgroundColor: '#f3fef3', width: '80%', alignSelf: 'center', marginTop: 10, borderColor: '#42b549' }}>
                <Text style={{ textAlign: 'center', color: '#42b549', margin: 10 }}>Harap transfer sesuai hingga tiga digit terakhir</Text>
            </View>
            <TouchableOpacity style={{ alignItems: 'center' }}>
                <View style={{ flexDirection: 'row', marginTop: 15 }}>
                    <Text style={{ fontSize: 12, fontWeight: '300', color: '#42b549', textAlign: 'center', marginRight: 5 }}>Lihat detail transaksi</Text>
                    <Image source={{ uri: icons.green_arrow_down }} style={{ marginTop: 3, width: 10, height: 10, resizeMode: 'contain' }} />
                </View>
            </TouchableOpacity>
        </View>

        <View style={{ alignItems: 'center', marginBottom: 20, marginTop: 20 }}>
            <View style={{ borderBottomColor: '#f1f1f1', borderBottomWidth: 1, width: '90%' }} />
        </View>

        <View style={{ alignItems: 'center' }}>
            <Image source={{ uri: this.props.data.bank_logo }} style={{ width: 100, height: 50, resizeMode: 'contain' }} />
            <Text style={{ color: 'rgba(0, 0, 0, 0.7)', fontSize: 14, marginTop: 5 }}>a/n {this.props.data.bank_holder}</Text>
            <Text style={{ color: 'rgba(0, 0, 0, 0.7)', fontSize: 14 }}>Cabang {this.props.data.bank_info}</Text>
            <Text style={{ color: 'rgba(0, 0, 0, 0.7)', fontSize: 18, fontWeight: '600' }}>{this.props.data.bank_num}</Text>
        </View>
        <View style={{ borderBottomColor: '#f1f1f1', borderBottomWidth: 1, marginTop: 35 }} />
        <View style={{ alignItems: 'center', marginTop: 10 }}>
            <TouchableOpacity style={{ backgroundColor: '#42b549', borderRadius: 3, width: '80%' }}>
                <Text style={{ fontSize: 14, fontWeight: '300', color: '#ffffff', margin: 15, textAlign: 'center' }}>Unggah Bukti / Cek Status Pembayaran</Text>
            </TouchableOpacity>
        </View>
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


const mapStateToProps = (state) => {
    console.log(state)
    return {
        dataDigital: state.dataDigital
    }
}

export default connect(mapStateToProps)(SuccessScreen)