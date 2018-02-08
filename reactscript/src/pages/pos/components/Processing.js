import React, { Component } from 'react'
import { View, Text, Image } from 'react-native'

export default class Processing extends Component {
    render() {
        return (
            <View style={{ flex: 1, backgroundColor: 'white' }}>
                <View style={{ width: "100%", marginTop: "60%", flexDirection: 'column',alignItems: 'center', justifyContent: 'center'}}>
                    <Image source={{ uri: 'https://ecs7.tokopedia.net/img/android_o2o/loading.gif' }}  style={{width: 110, height: 110}} />
                    <Text style={{fontSize:17, marginTop:"2%"}}>Pembayaran sedang diproses...</Text>
                </View>
            </View>
        )
    }
}