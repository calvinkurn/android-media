import React, { Component } from 'react'
import { View, TouchableOpacity, Text } from 'react-native'
import { NavigationModule } from 'NativeModules'



class InvoiceErrorPage extends Component {
    render (){
        return (
            <View style={{ flex: 1, justifyContent: 'center' }}>
                <Text style={{ 
                    textAlign: 'center', 
                    fontSize: 45, 
                    fontWeight: '600' }}>{this.props.screenProps.data.errorTitle}</Text>
                <Text style={{ 
                    textAlign: 'center', 
                    fontSize: 30, 
                    fontWeight: '400' }}>{this.props.screenProps.data.errorMessage}</Text>
                <TouchableOpacity 
                    onPress={() => NavigationModule.navigate('posapp://cart', '')}
                    style={{ 
                        alignSelf: 'center',
                        width: '80%',
                        backgroundColor: '#3CB742', 
                        alignItems: 'center',
                        borderRadius: 5,
                        marginTop: 35 }}>
                    <Text style={{ fontSize: 30, color: 'white', margin: 20 }}>Back to Cart</Text>
                </TouchableOpacity>
            </View>
        )
    }
}

export default InvoiceErrorPage