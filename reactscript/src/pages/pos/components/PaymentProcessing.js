import React, { Component } from 'react';
import { connect } from 'react-redux';
import { StyleSheet, Text, View, Button, Image, TouchableWithoutFeedback} from 'react-native';


class PaymentProcessing extends Component {
  componentDidMount(){
    console.log(this.props)
    // setTimeout(
    //   () => { 
    //     this.props.navigation.navigate('PaymentInvoice', {})
    //   }, 
    //   4000
    // );
  }

  render() {
    console.log(this.props)

    // if (!this.props.isFetchingParams && !this.props.showLoadingPage && this.props.status_msg === 'SUCCESS'){
    //   this.props.navigation.navigate('PaymentInvoice', {})
    // }

    return (
      <View style={{ flex: 1 }}>
        <View style={{ width: "100%", marginTop: "60%", flexDirection: 'column',alignItems: 'center', justifyContent: 'center'}}>
          <Image source={{ uri: 'https://ecs7.tokopedia.net/img/android_o2o/loading.gif' }}  style={{width: 110, height: 110}} />
          <Text style={{fontSize:17, marginTop:"2%"}}>Pembayaran sedang diproses...</Text>
        </View>
      </View>
    )
  }

  static navigationOptions = {
    // title: 'Payment Processing',
    // headerTintColor: '#FFF',
    // headerStyle: {
    //     backgroundColor: '#42B549'
    // },
    header: null
    // headerMode: 'none'
  };
  

}

const mapStateToProps = state => {
  console.log(state.paymentV2)
  return {
    // ...state.payment,
    // isFetchingParams: state.paymentV2.isFetchingParams,
    // showLoadingPage: state.paymentV2.showLoadingPage,
    // status_msg: state.paymentV2.status_msg
  }
}

export default connect(mapStateToProps)(PaymentProcessing)