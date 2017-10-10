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
    const data_process = this.props.screenProps.data.data_process
    const data_process_json = JSON.parse(data_process)

    this.props.dispatch(makePayment(data_process_json.checkout_data, data_process_json.selectedEmiId, 
      data_process_json.ccNum, data_process_json.mon + '/' + data_process_json.year, data_process_json.cvv))
  }



  render() {
    if (this.props.processing_status_msg === 'SUCCESS' && !this.props.processing_isFetchingParams && !this.props.processing_showLoadingPage){
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
    header: null
  }
}

const mapStateToProps = state => {
  return {
    ...state.paymentV2,
    processing_isFetchingParams: state.paymentV2.processing_isFetchingParams,
    processing_showLoadingPage: state.paymentV2.processing_showLoadingPage,
    processing_data: state.paymentV2.processing_data,
    processing_status_msg: state.paymentV2.processing_status_msg
  }
}

export default connect(mapStateToProps)(PaymentProcessing)