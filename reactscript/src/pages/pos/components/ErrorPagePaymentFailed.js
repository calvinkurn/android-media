import React, { Component } from 'react'
import { View, Text, Image } from 'react-native'



class ErrorPagePaymentFailed extends Component {
    render(){
        return (
            <View>
                <Text>Transaksi Anda Gagal</Text>
                <Text>Dana terpakai telah berhasil dikembalikan ke Limit Kartu Kredit Anda</Text>
            </View>
        )
    }
}

export default ErrorPagePaymentFailed