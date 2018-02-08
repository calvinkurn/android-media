import React, { Component } from 'react'
import { View, TouchableOpacity, Text, Image } from 'react-native'
import { NavigationModule } from 'NativeModules'
import { connect } from 'react-redux'
import { reloadState } from '../../../actions/index'
import { icons } from '../../../lib/config'


class InvoiceErrorPage extends Component {
    componentDidMount(){
        const { dispatch } = this.props
        dispatch(reloadState('invoice'))
    }


    render (){
        return (
            <View style={{ flex: 1, backgroundColor: '#f4f4f4' }}>
                <View style={{
                    borderRadius: 2,
                    backgroundColor: 'white', 
                    margin: 30 }}>
                    <Image source={{ uri: icons.error_icon }} 
                        style={{
                            marginTop: 70,
                            alignSelf: 'center',
                            width: 400, 
                            height: 200, 
                            resizeMode: 'contain' }} />
                    <Text style={{ 
                        marginTop: 20,
                        textAlign: 'center', 
                        fontSize: 20, 
                        fontWeight: '600' }}>{this.props.screenProps.data.errorTitle}</Text>
                    <Text style={{ 
                        marginTop: 12,
                        textAlign: 'center', 
                        fontSize: 18, 
                        fontWeight: '400' }}>{this.props.screenProps.data.errorMessage}</Text>
                    <TouchableOpacity 
                        onPress={() => NavigationModule.navigate('posapp://cart', '')}
                        style={{ 
                            alignSelf: 'center',
                            width: '50%',
                            backgroundColor: '#42b549', 
                            alignItems: 'center',
                            borderRadius: 3,
                            marginBottom: 55,
                            marginTop: 35 }}>
                        <Text style={{ fontSize: 20, color: 'white', margin: 20 }}>Checkout Ulang</Text>
                    </TouchableOpacity>
                </View>
            </View>
        )
    }
}

export default connect()(InvoiceErrorPage)