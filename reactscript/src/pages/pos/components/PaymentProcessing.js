import React, { Component } from 'react';
import { connect } from 'react-redux';
import { StyleSheet, Text, View, Button, Image, TouchableWithoutFeedback} from 'react-native';
import { makePayment } from '../actions/index'
import { NavigationModule } from 'NativeModules'


class PaymentProcessing extends Component {
  componentDidMount(){
    this.payment_v2_processing()
  }

  payment_v2_processing = () => {
    console.log(this.props)

    // console.log(this.props)
    // const data_process = JSON.parse(this.props.data.data_process)
    // console.log(data_process)
    // console.log(data_process.checkout_data)

    const data_process = this.props.screenProps.data.data_process
    const data_process_json = JSON.parse(data_process)
    console.log(data_process_json)
    console.log(data_process_json.checkout_data)
    // const checkout_data = JSON.parse(data_process_json.checkout_data)
    // console.log(data_process_json)
    // console.log(checkout_data)

    this.props.dispatch(makePayment(data_process_json.checkout_data, data_process_json.selectedEmiId, 
      data_process_json.ccNum, data_process_json.mon + '/' + data_process_json.year, data_process_json.cvv));
    

  }



  render() {
    console.log(this.props)
    if (this.props.processing_status_msg === 'SUCCESS'){
      NavigationModule.navigate('posapp://payment/otp', JSON.stringify(this.props.processing_data))
    }

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
    ...state.paymentV2,
    // processing_data
    processing_url: state.paymentV2.processing_data.url,
    processing_isFetchingParams: state.paymentV2.processing_isFetchingParams,
    processing_showLoadingPage: state.paymentV2.processing_showLoadingPage,
    processing_data: state.paymentV2.processing_data,
    processing_status_msg: state.paymentV2.processing_status_msg
  }
}

export default connect(mapStateToProps)(PaymentProcessing)