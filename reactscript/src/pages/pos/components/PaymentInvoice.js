import React, { Component } from 'react';
import { connect } from 'react-redux';
import { StyleSheet, Text, View, Image, Button, TouchableWithoutFeedback, ScrollView, TextInput, TouchableNativeFeedback, ListView} from 'react-native';
import { emailValidation } from '../lib/utility'
import PopupDialog, {DialogTitle} from 'react-native-popup-dialog';
import { NavigationModule } from 'NativeModules'

import PopUp from '../common/TKPPopupModal'

class PaymentInvoice extends Component {

  constructor (props) {

    super (props);

    this.state = {
      email: "",
      emailErrorMessage : ""
    };
  }

  _handleButtonPress = () => {
    let emailErrorMessage  = "";
    if (!emailValidation(this.state.email)) {
      emailErrorMessage = "Mohon masukan alamat email Anda dengan format contoh@email.com";
    } else {
      this.popupDialog.show()
    }
    this.setState({
      emailErrorMessage
    });
  };

  _renderProductList(rowData: string, sectionID: number, rowID: number) {
    return (  
      <View style={{flex: 1, flexDirection: 'row',justifyContent: 'space-between', marginTop: '6%'}}>
          <View style={{width: "20%", height: '20%'}}>
              <Image source={ {uri: rowData.imageUrl} } style={styles.productImage} ></Image>
          </View>
          <View style={{width: '60%', height: '20%', flexDirection: 'column', justifyContent: 'flex-start'}}>
              <Text style={{fontSize: 22, fontWeight: 'bold', width: '80%'}}>{rowData.name} </Text> 
              <Text style={{fontSize: 20, color: '#C8C8C8', marginTop: 10}}>Jumlah Barang: {rowData.qty}</Text> 
          </View>
          <Text style={{fontSize: 22, fontWeight: 'bold'}}>{rowData.price}</Text> 
      </View>
    );
}

  toggleScreen = (visible) => {
    this.setState({ showPopUp: visible })
  }

  render() {
    return (
      <View style={{ flex: 1 }}>
        <View style={{
        height: 55,
        backgroundColor: '#42b549',
        flexDirection: 'row',
        alignItems: 'center',
        justifyContent: 'space-around'
        }}>
          <View style={{ width: '5%', left: 10 }}>
            
          </View>
          <View style={{ width: '90%'}}>
            <Text style={{ fontSize: 20, color: '#fff' }}>Invoice</Text>
          </View>
        </View>
         <ScrollView>
              <View style={styles.containers} >
                <View style={[styles.row, styles.row1]} >
                  <Image source={require('./img/Success_icon.png')} />
                  <Text style={styles.text1}> Transaksi Berhasil! </Text>
                  <Text style={styles.text2}> Terima kasih telah berbelanja di toko kami</Text>
                  <Text style={styles.text3}> IVR/20170609/XVII/VI/13461162</Text>
                </View>

                <ListView
                    contentContainerStyle={[styles.row, styles.row2]}
                    dataSource = { this.props.itemList}
                    enableEmptySections={true}
                    renderRow = {this._renderProductList.bind(this)} />

                <View style={[styles.row, styles.row4]} >
                   <View style={{flexDirection: 'column'}}>
                    <Text style={{fontSize: 20}}>Metode Pembayaran</Text>
                    <View style={{flexDirection: 'row', alignItems: 'center' }}>
                      <Image source={require('./img/Logo-BCA.png')} style={{height: 50, width: 110, resizeMode: 'cover'}} >
                      </Image>
                      <Text style={{fontSize: 22, marginLeft: 10}}>BCA</Text>
                    </View>
                   </View>
                    <View style={{flexDirection: 'row'}}>
                      <Text style={{fontSize: 25}}>Total Pembayaran</Text>
                      <Text style={{fontSize: 25, fontWeight: 'bold'}}> {this.props.totalPrice}</Text>
                    </View>
                </View>

                <View style={[styles.row, styles.row3]} >
                  <Text style={styles.row3text1}> Bukti Pembayaran </Text>
                   <TextInput
                      style={{fontSize:25, width:"100%", marginTop:10}}
                      placeholder="Email"
                      underlineColorAndroid="#C6C6C6"
                      placeholderTextColor = "#C6C6C6"
                      onChangeText={(email) => this.setState({email})}
                    />
                  <Text style={styles.row3text2}> Masukan email untuk kirim bukti pembayaran</Text>
                  <Text style={[styles.row3text2, styles.errorText, {marginTop:5}]}> 
                    { this.state.emailErrorMessage }
                  </Text>
                  <View style={{ flexDirection:'row',flex:1,justifyContent:'space-between', alignItems:'center', marginTop: "5%"}} >
                    <TouchableNativeFeedback 
                      onPress={() => NavigationModule.navigate("posapp://product", "")}>
                      <View style={[styles.button, {backgroundColor: "#FFFFFF", borderColor:"#F3F3F3"}]}>
                        <Text style={[styles.buttonText, {color: "#888888"}]}> Lewati </Text>
                      </View>
                    </TouchableNativeFeedback>
                     <TouchableNativeFeedback onPress={this._handleButtonPress}>
                      <View style={[styles.button, {marginLeft: "2%"}]}>
                        <Text style={styles.buttonText}> Kirim </Text>
                      </View>
                    </TouchableNativeFeedback>
                  </View>
                </View>
              </View>
          </ScrollView>
          
          <PopupDialog
                dialogTitle={
                  <View style={{flexDirection:'row', justifyContent: 'flex-end', padding: 10}}> 
                    <TouchableWithoutFeedback onPress={() => { this.popupDialog.dismiss()}}>
                    <Image source={require('../common/img/close-icon.png')} />
                    </TouchableWithoutFeedback>
                  </View>
                }
                dialogStyle={{borderRadius: 10}}
                width = {700}
                height= {300}
                ref={(popupDialog) => { this.popupDialog = popupDialog; }}>
              <View style={{padding: 30, paddingTop: 0, flex: 1, flexDirection: 'column', justifyContent: 'space-between', alignItems: 'center'}}>
                <Text style={{fontSize: 25, fontWeight: 'bold'}}>
                  Bukti Pembayaran
                </Text>
                <Text style={{fontSize: 22}}>
                  Bukti pembayaran sudah terkirim ke email Anda
                </Text>
                <TouchableNativeFeedback onPress={() => { this.popupDialog.dismiss()}}>
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
    title: 'Payment Invoice',
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
      row1:{
        flexDirection: 'column',
        justifyContent: 'center',
        alignItems: "center",
        padding: "3%"
      },
      row2 : {
        flexDirection: 'column',
        justifyContent: 'flex-start',
        alignItems: 'flex-start',
        padding: "3%",
        paddingTop: 0
      },
      row3:{
        flexDirection: 'column',
        justifyContent: 'flex-start',
        alignItems: "flex-start",
        padding: "3%",
        marginTop: "2%"
      },
      row4:{
        flexDirection: 'row',
        justifyContent: 'space-between',
        padding: "3%",
      },
      text1: {
        fontSize: 25,
        fontWeight: 'bold',
        marginTop: 10
      },
      text2: {
        fontSize: 25,
        color:"#C8C8C8"
      },
      text3: {
        marginTop: 10,
        fontSize: 22,
        fontWeight: 'bold'
      },
      row3text1: {
        fontSize: 22,
        fontWeight: 'bold',
        marginTop: 10
      },
      row3text2: {
        fontSize: 20,
        color:"#C8C8C8"
      },
       button: {
        height: 50,
        width: "48%",
        backgroundColor: '#41B548',
        alignItems: 'center',
        justifyContent: 'center',
        borderRadius: 3,
        borderWidth: 3,
        borderColor: '#41B548',
      },
      popupButton: {
        height: 50,
        width: "90%",
        backgroundColor: '#41B548',
        alignItems: 'center',
        justifyContent: 'center',
        borderRadius: 3,
        borderWidth: 3,
        borderColor: '#41B548',
      },
      buttonText: {
        color: '#FFFFFF',
        fontSize: 22,
      },
      errorText : {
        color: '#D50000'
      },
      productImage: {
        borderRadius: 3,
        width: 100,
        height: 100,
        resizeMode: 'cover'
      },
  });

const ds = new ListView.DataSource({
      rowHasChanged: (r1, r2) => r1 !== r2 });

const mapStateToProps = state => {
  const itemList = ds.cloneWithRows(state.paymentInvoice.items);
  return {
    ...state.paymentInvoice,
    itemList
  }
}

export default connect(mapStateToProps)(PaymentInvoice)