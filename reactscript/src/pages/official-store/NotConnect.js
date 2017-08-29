import React, { Component } from 'react'
import { View, Text, Image, TouchableOpacity, Dimensions } from 'react-native'
import { icons } from '../../icons/index'
import { NetworkModule } from 'NativeModules'
import Ostore from './setup'

const { height, width } = Dimensions.get('window');

class NoConnection extends Component {
    constructor(props){
        super(props)

        this.state = {
            isConnected: false
        }
    }

    // clickHandler(){
    //     NetworkModule.getResponse('http://mojito.tokopedia.com/os/api/v1/brands/list?device=lite&microsite=true&user_id=0&limit=1&offset=0', 'GET', '', true)
    //         .then(response => {
    //             console.log('sukses')
    //             let jsonResponse = JSON.parse(response) 
    //             this.setState({ isConnected: true })
    //         })
    //         .catch(err => {
    //             console.log('gagal: ', err)
    //         })
    // }

    render (){
        if (!this.state.isConnected){
            return (
                <View style={{ flex: 1, justifyContent: 'center', alignItems: 'center', backgroundColor: '#E7E7E7' }}>
                    <Image source={ icons.offline } style={{ marginBottom: 15, resizeMode: 'contain', width: 0.4 * width }} />
                    <Text style={{ fontSize: 22, fontWeight:'bold', color: 'black', marginBottom: 10 }}>Terjadi kesalahan koneksi</Text>
                    <Text style={{ fontSize: 16, fontWeight:'600', color: 'grey', marginBottom: 30 }}>Silahkan coba lagi</Text>
                    <TouchableOpacity 
                        onPress={() => this.setState({ isConnected: true })}
                        style={{ marginBottom: 10 }}>
                        <Text style={{ fontSize: 18, fontWeight:'bold', color: 'black', color: '#42B549' }}>COBA LAGI</Text>
                    </TouchableOpacity>
                </View>
            )
        } else {
            return <Ostore Screen={'official-store'} />
        }
    }
} 

export default NoConnection