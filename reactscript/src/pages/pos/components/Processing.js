import React, { Component } from 'react'
import { View, Text, Image } from 'react-native'
import { NavigationModule } from 'NativeModules'
import { connect } from 'react-redux'
import { PaymentCheckoutToNative } from '../actions/index'


class Processing extends Component {
    componentDidMount(){
        this.props.dispatch(PaymentCheckoutToNative())

        // setTimeout(
        //     () => {
        //         NavigationModule.navigateAndFinish(`posapp://payment/checkout?total_payment=${this.props.totalPrice}`, "")
        //     },  4000
        // )
        
    }

    render() {
        console.log(this.props)
        
        return (
            <View style={{ flex: 1, backgroundColor: 'white' }}>
                {!this.props.checkoutData.isFetching && 
                    NavigationModule.navigateAndFinish(`posapp://payment/checkout?total_payment=${this.props.totalPrice}`, "")
                }
                <View style={{ width: "100%", marginTop: "60%", flexDirection: 'column',alignItems: 'center', justifyContent: 'center'}}>
                    <Image source={{ uri: 'https://ecs7.tokopedia.net/img/android_o2o/loading.gif' }}  style={{width: 110, height: 110}} />
                    <Text style={{fontSize:17, marginTop:"2%"}}>Pembayaran sedang diproses...</Text>
                </View>
            </View>
        )
    }
}

const mapStateToProps = (state) => {
    console.log(state)
    return {
        data: state,
        checkoutData: state.checkout 
    }
}

export default connect(mapStateToProps)(Processing)