import React, { Component } from 'react';
import { connect } from 'react-redux';
import { StyleSheet, Text, View, Button, Image, TouchableWithoutFeedback} from 'react-native';
import { makePayment } from '../../../actions/index'
import { NavigationModule } from 'NativeModules'
import { icons } from '../../../lib/config'


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
    const { 
      processing_status_msg, 
      processing_isFetchingParams, 
      processing_showLoadingPage, 
      processing_data,
      selectedBankData,
    } = this.props

    // console.log(processing_data)
    // console.log(selectedBankData)

    
    if (processing_status_msg === 'SUCCESS' && !processing_isFetchingParams && !processing_showLoadingPage){
      const { transaction_id } = this.props.checkoutData.data.data.data
      const objData = {
        ...processing_data, 
        bank_id: selectedBankData.bank_id,
        bank_logo: selectedBankData.bank_logo,
        bank_name: selectedBankData.bank_name,
        transaction_id
      }
      // console.log(objData)

      NavigationModule.navigate('posapp://payment/otp', JSON.stringify(objData))
    }

    return (
      <View style={{ flex: 1 }}>
        <View style={{ width: "100%", marginTop: "60%", flexDirection: 'column',alignItems: 'center', justifyContent: 'center'}}>
          <Image source={{ uri: icons.loading }}  style={{width: 110, height: 110}} />
          <Text style={{fontSize:17, marginTop:"2%"}}>Pembayaran sedang diproses...</Text>
        </View>
      </View>
    )
  }

  static navigationOptions = {
    header: null
  }
}

const mapStateToProps = (state, ownProps) => {
  const data_process_json = JSON.parse(ownProps.screenProps.data.data_process)
  // console.log(data_process_json)
  // console.log(state)
  // console.log(ownProps)
  // console.log(state.checkout)

  return {
    ...state.paymentV2,
    processing_isFetchingParams: state.paymentV2.processing_isFetchingParams,
    processing_showLoadingPage: state.paymentV2.processing_showLoadingPage,
    processing_data: state.paymentV2.processing_data.data,
    processing_status_msg: state.paymentV2.processing_status_msg,
    selectedBankData: data_process_json.selectedBankData,
    checkoutData: state.checkout,
  }
}

export default connect(mapStateToProps)(PaymentProcessing)