import React, { Component } from 'react';
import { connect } from 'react-redux';
import { StyleSheet, Text, View, Button, Image, TouchableWithoutFeedback} from 'react-native';

class PaymentProcessing extends Component {
  componentDidMount(){
    setTimeout(
      () => { 
        this.props.navigation.navigate('PaymentInvoice', {})
      }, 
      4000
    );
  }

  render() {
    return (
      <View style={{ flex: 1 }}>
        {/* <View style={{
          height: 55,
          backgroundColor: '#42b549',
          flexDirection: 'row',
          alignItems: 'center',
          justifyContent: 'space-around' }}>
          <View style={{ width: '10%', left: 10 }}></View>
          <View style={{ width: '80%', left: 10 }}>
            <Text style={{ fontSize: 18, color: '#fff' }}>Pembayaran</Text>
          </View>
          <View style={{ width: '10%' }}></View>
        </View> */}
        
        <View style={{ width: "100%", marginTop: "60%", flexDirection: 'column',alignItems: 'center', justifyContent: 'center'}}>
          <Image source={require('./img/loading.gif')} />
              <Text style={{fontSize:17, marginTop:"2%"}}>Pembayaran sedang diproses...</Text>
        </View>
      </View>
    )
  }

  static navigationOptions = {
    title: 'Payment Processing',
  };

}

const mapStateToProps = state => {
  return {
    ...state.payment,
  }
}

export default connect(mapStateToProps)(PaymentProcessing)