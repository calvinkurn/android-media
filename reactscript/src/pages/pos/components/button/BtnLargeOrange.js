import React, { Component } from 'react'
import { View, Text, TouchableOpacity } from 'react-native'

export default class BtnLargeOrange extends Component {
    render(){
        return(
            <TouchableOpacity style={{ backgroundColor: '#ff5722', borderRadius:6 }}>
                <Text style={{ color:'#FFF', textAlign:'center', fontSize:20, marginTop:12, marginBottom:12 }}>{this.props.BtnTxt}</Text>
            </TouchableOpacity>
        )
    }
}