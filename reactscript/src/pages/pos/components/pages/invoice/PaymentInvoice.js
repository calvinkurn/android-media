import React, { Component } from 'react';
import { connect } from 'react-redux';
import { FlatList, StyleSheet, View, Image, Button, TouchableWithoutFeedback, ScrollView, TextInput, TouchableNativeFeedback, ListView } from 'react-native';
import { emailValidation } from '../../../lib/utility'
import PopupDialog, { DialogTitle } from 'react-native-popup-dialog';
import { NavigationModule } from 'NativeModules'
import Dash from 'react-native-dash';

import PopUp from '../../../common/TKPPopupModal'
import { Text } from '../../../common/TKPText'
import { reloadState, clearCart, sendEmailReceipt } from '../../../actions/index'
import { icons } from '../../../lib/config'



class PaymentInvoice extends Component {
  constructor(props) {
    super(props)
    
    this.state = {
      email: "",
      emailErrorMessage: "",
      sendEmail: false
    }
  }
  
  
  componentDidMount(){
    const { dispatch } = this.props
    console.log(this.props)
    
    // dispatch(reloadState('invoice'))
    // dispatch(clearCart())
  }
  
  
  _handleButtonPress = () => {
    let emailErrorMessage = ""
    if (!emailValidation(this.state.email)) {
      emailErrorMessage = "Mohon masukan alamat email Anda dengan format contoh@email.com"
    } else {
      this._sendInvoice()
    }
    
    this.setState({ emailErrorMessage })
  }


  _skipButton = () => {
    console.log('skipppp')
    if (!this.state.email){
      this.props.dispatch(reloadState('invoice'))
      this.props.dispatch(clearCart())
    }
    NavigationModule.navigateAndFinish("posapp://product", "")
  }



  _sendInvoice = () => {
    // console.log(this.props)
    const {
      bankName,
      bankLogo,
      payment_param,
      screenProps,
    } = this.props
    const { email } = this.state

    const payment_details = JSON.parse(screenProps.data.data)
    // console.log(payment_details)

    const data = {
      email_address: email,
      bank_name: bankName,
      bank_logo: bankLogo,
      transaction_date: payment_param.transaction_date,
      final_amount: payment_details.paymentDetails[0].amount,
      invoice_ref_no: payment_details.invoiceRef
    }

    const items = payment_param.items

    // console.log(this.state.email, data, items)
    this.props.dispatch(sendEmailReceipt(email, data, items))
    this.props.dispatch(reloadState('invoice'))
    this.props.dispatch(clearCart())
    this.setState({ sendEmail: true })
    this.popupDialog.show()
  }



  _renderProductList = ({item}) => {
    return (
      <View style={{ marginLeft: 20, marginRight: 20, flex: 1, flexDirection: 'row', justifyContent: 'space-between', marginTop: '1%', backgroundColor: 'white' }}>
        <View style={{ width: "12%", height: '20%' }}>
          <Image source={{ uri: item.imageUrl }} style={styles.productImage} ></Image>
        </View>
        <View style={{ width: '76%', height: '20%', flexDirection: 'column', justifyContent: 'flex-start' }}>
          <Text style={[styles.font14, styles.fontcolor71, { width: '80%' }]}>{item.name} </Text>
          <Text style={[styles.font13, styles.fontcolor61, { marginTop: 10 }]}>Jumlah Barang: {item.quantity}</Text>
        </View>
        <Text style={[styles.font14, styles.fontcolor71]}>Rp {(item.price).toLocaleString("id")}</Text>
      </View>
    )
  }


  // toggleScreen = (visible) => {
  //   this.setState({ showPopUp: visible })
  // }


  render() {
    // console.log(this.props)
    const data = JSON.parse(this.props.screenProps.data.data)
    const paymentDetails = data.paymentDetails

    return (
      <View style={{ flex: 1 }}>
        <ScrollView>
          <View style={styles.containers} >
            <View style={[styles.row, styles.row1]} >
              <Image style={{ width: 111, height: 85, resizeMode: 'contain' }} source={{ uri: icons.success }} />
              <Text style={styles.text1}> Transaksi Berhasil! </Text>
              <Text style={styles.text2}> Terima kasih telah berbelanja di toko kami</Text>
              <Text style={styles.text3}>{data.invoiceRef}</Text>
            </View>

            <View style={{ backgroundColor: 'white' }}>
              <FlatList 
                data={data.items}
                keyExtractor={item => item.id}
                renderItem={this._renderProductList}
                numColumns={1} 
                style={{ backgroundColor: 'white' }} />
            </View>
            
            <View style={{paddingRight: '2%', paddingLeft: '3%', backgroundColor: '#FFFFFF'}}>
              <Dash style={{width:"100%", height:2}} dashColor="#F0F0F0"/>
            </View>

            <View style={[styles.row, styles.row4]} >
              <View style={{ flexDirection: 'column' }}>
                <Text style={{ fontSize: 13, color: '#00000061' }}>Metode Pembayaran</Text>
                <View style={{ flexDirection: 'row', alignItems: 'center' }}>
                  <Image source={{ uri: this.props.bankLogo }} style={{ height: 48, width: 70, resizeMode: 'contain' }} >
                  </Image>
                  <Text style={{ fontSize: 15, color: '#0000008a', marginLeft: 10 }}>{this.props.bankName}</Text>
                </View>
              </View>
              <View style={{ flexDirection: 'row' }}>
                <Text style={{ fontSize: 16, color: '#0000008a' }}>Total Pembayaran</Text>
                <Text style={{ fontSize: 16, color: '#000000b3' }}> Rp {(paymentDetails[0].amount).toLocaleString("id")}</Text>
              </View>
            </View>
                <View style={[styles.row, styles.row3]} >
                  <Text style={styles.row3text1}> Bukti Pembayaran </Text>
                  {(this.state.emailErrorMessage == "") ? 

                      <TextInput
                        style={{fontSize:16, width:"100%", marginTop:10}}
                        placeholder="Email"
                        underlineColorAndroid="#e0e0e0"
                        placeholderTextColor = "#00000042"
                        onChangeText={(email) => this.setState({email})} />
                      : 
                      <TextInput
                        style={{fontSize:16, width:"100%", marginTop:10}}
                        placeholder="Email"
                        underlineColorAndroid="#D50000"
                        placeholderTextColor = "#00000042"
                        onChangeText={(email) => this.setState({email})} />
                  }
                   
                    {(this.state.emailErrorMessage == "") ? 

                <Text style={styles.row3text2}> Masukan email untuk kirim bukti pembayaran</Text>
                :
                <Text style={[styles.row3text2, styles.errorText, { marginTop: 5 }]}>
                  {this.state.emailErrorMessage}
                </Text>
              }
              <View style={{ flexDirection: 'row', flex: 1, justifyContent: 'space-between', alignItems: 'center', marginTop: "5%" }} >
                <TouchableNativeFeedback
                  onPress={() => { this._skipButton() }}>
                  <View style={[styles.button, { backgroundColor: "#FFFFFF", borderColor: "#e0e0e0", marginLeft: "10%" }]}>
                    <Text style={[styles.buttonText, { color: "#0000008a" }]}> Lewati </Text>
                  </View>
                </TouchableNativeFeedback>
                <TouchableNativeFeedback onPress={this._handleButtonPress}>
                  <View style={[styles.button, { marginLeft: "2%" }]}>
                    <Text style={styles.buttonText}> Kirim </Text>
                  </View>
                </TouchableNativeFeedback>
              </View>
            </View>
          </View>
        </ScrollView>

        
          <PopupDialog
            dialogTitle={
              <View style={{ flexDirection: 'row', justifyContent: 'flex-end', padding: 10 }}>
              </View>
            }
            dialogStyle={{ borderRadius: 10 }}
            width={504}
            height={232}
            ref={(popupDialog) => { this.popupDialog = popupDialog; }}>
            <View style={{ padding: 30, paddingTop: 0, flex: 1, flexDirection: 'column', justifyContent: 'space-between', alignItems: 'center' }}>
              <Text style={{ fontSize: 20, color: '#000000b3' }}>
                Bukti Pembayaran
                  </Text>
              <Text style={{ fontSize: 14, color: '#000000b3' }}>
                Bukti pembayaran sudah terkirim ke email Anda
                  </Text>
              <TouchableNativeFeedback onPress={() => { this.popupDialog.dismiss() }}>
                <View style={[styles.popupButton]}>
                  <Text style={styles.buttonText}> Ok </Text>
                </View>
              </TouchableNativeFeedback>
            </View>
          </PopupDialog>
        
      </View>
    )
  }

  static navigationOptions = {
    header: null
  };

}


const styles = StyleSheet.create({
  containers: {
    flex: 1,
    alignItems: 'stretch',
    margin: 20,
  },
  row: {
    backgroundColor: '#FFFFFF',
    borderBottomWidth: 1,
    borderColor: '#F0F0F0'
  },
  row1: {
    flexDirection: 'column',
    justifyContent: 'center',
    alignItems: "center",
    padding: "3%"
  },
  row2: {
    flexDirection: 'column',
    justifyContent: 'flex-start',
    alignItems: 'flex-start',
    borderBottomWidth: 0,
    padding: "3%",
    paddingTop: 0
  },
  row3: {
    flexDirection: 'column',
    justifyContent: 'flex-start',
    alignItems: "flex-start",
    padding: "3%",
    borderBottomWidth: 0,
    marginTop: "2%"
  },
  row4: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    padding: "3%",
    paddingRight: "2%"
  },
  text1: {
    fontSize: 16,
    color: '#000000b3',
    marginTop: 10
  },
  text2: {
    fontSize: 16,
    color: "#00000061"
  },
  text3: {
    marginTop: 10,
    fontSize: 14,
    color: "#000000b3"
  },
  row3text1: {
    fontSize: 12,
    color: '#000000b3',
    marginTop: 10
  },
  row3text2: {
    fontSize: 12,
    color: "#00000061"
  },
  button: {
    height: 40,
    width: 252,
    backgroundColor: '#42b549',
    alignItems: 'center',
    justifyContent: 'center',
    borderRadius: 3,
    borderWidth: 3,
    borderColor: '#42b549',
  },
  popupButton: {
    height: 40,
    width: 432,
    backgroundColor: '#42b549',
    alignItems: 'center',
    justifyContent: 'center',
    borderRadius: 3,
    borderWidth: 3,
    borderColor: '#42b549',
  },
  buttonText: {
    color: '#FFFFFF',
    fontSize: 13,
  },
  errorText: {
    color: '#D50000'
  },
  productImage: {
    borderRadius: 3,
    width: 62,
    height: 62,
    resizeMode: 'cover'
  },
  font13: {
    fontSize: 13
  },
  fontcolor61: {
    color: "#00000061"
  },
  font15: {
    fontSize: 15
  },
  font14: {
    fontSize: 14
  },
  font16: {
    fontSize: 16
  },
  fontcolor54: {
    color: "#0000008a"
  },
  fontcolor70: {
    color: "#000000b3"
  },
});

// const ds = new ListView.DataSource({
//   rowHasChanged: (r1, r2) => r1 !== r2
// });

const mapStateToProps = (state, ownProps) => {
  const objData = JSON.parse(ownProps.screenProps.data.data)
  // const itemList = ds.cloneWithRows(state.paymentInvoice.items);
  // console.log(state)
  // console.log(ownProps)
  return {
    // ...state.paymentInvoice,
    // itemList,
    bankLogo: objData.bankLogo, 
    bankName: objData.bankName,
    invoiceNo: objData.invoiceRef,
    payment_param: state.checkout.data.payment_param,
    email_isSucceed: state.sendEmailResponse.isSuccees,
    email_message: state.sendEmailResponse.message,
  }
}

export default connect(mapStateToProps)(PaymentInvoice)

