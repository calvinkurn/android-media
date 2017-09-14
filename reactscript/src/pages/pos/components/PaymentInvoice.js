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
       <View style={{flex: 1, flexDirection: 'row',justifyContent: 'space-between', marginTop: '1%'}}>
          <View style={{width: "15%", height: '20%'}}>
              <Image source={ {uri: rowData.imageUrl} } style={styles.productImage} ></Image>
          </View>
          <View style={{width: '70%', height: '20%', flexDirection: 'column', justifyContent: 'flex-start'}}>
              <Text style={[styles.font14, styles.fontcolor71, {width: '80%'}]}>{rowData.name} </Text> 
              <Text style={[styles.font13, styles.fontcolor61, {marginTop: 10}]}>Jumlah Barang: {rowData.qty}</Text> 
          </View>
          <Text style={[styles.font14, styles.fontcolor71]}>{rowData.price}</Text> 
      </View>
    );
}

  toggleScreen = (visible) => {
    this.setState({ showPopUp: visible })
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
          <View style={{ width: '5%', left: 10 }}></View>
          <View style={{ width: '90%'}}>
            <Text style={{ fontSize: 20, color: '#fff' }}>Invoice</Text>
          </View>
        </View> */}
         <ScrollView>
              <View style={styles.containers} >
                <View style={[styles.row, styles.row1]} >
                  <Image style={{width: 111, height: 85}} source={{ uri: 'https://ecs7.tokopedia.net/img/android_o2o/Success_icon.png' }} />
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
                    <Text style={{fontSize: 13, color: '#00000061'}}>Metode Pembayaran</Text>
                    <View style={{flexDirection: 'row', alignItems: 'center' }}>
                      <Image source={require('./img/Logo-BCA.png')} style={{height: 48, width: 24, resizeMode: 'cover'}} >
                      </Image>
                      <Text style={{fontSize: 15,color: '#0000008a', marginLeft: 10}}>BCA</Text>
                    </View>
                   </View>
                    <View style={{flexDirection: 'row'}}>
                      <Text style={{fontSize: 16, color: '#0000008a'}}>Total Pembayaran</Text>
                      <Text style={{fontSize: 16, color: '#000000b3'}}> {this.props.totalPrice}</Text>
                    </View>
                </View>

                <View style={[styles.row, styles.row3]} >
                  <Text style={styles.row3text1}> Bukti Pembayaran </Text>
                   <TextInput
                      style={{fontSize:16, width:"100%", marginTop:10}}
                      placeholder="Email"
                      underlineColorAndroid="#e0e0e0"
                      placeholderTextColor = "#00000042"
                      onChangeText={(email) => this.setState({email})}
                    />
                    {(this.state.emailErrorMessage == "") ? 

                      <Text style={styles.row3text2}> Masukan email untuk kirim bukti pembayaran</Text>
                    : 
                       <Text style={[styles.row3text2, styles.errorText, {marginTop:5}]}> 
                      { this.state.emailErrorMessage }
                    </Text>
                  }
                  <View style={{ flexDirection:'row',flex:1,justifyContent:'space-between', alignItems:'center', marginTop: "5%"}} >
                    <TouchableNativeFeedback 
                      onPress={() => { NavigationModule.navigateAndFinish("posapp://product", "") }}>
                      <View style={[styles.button, {backgroundColor: "#FFFFFF", borderColor:"#e0e0e0", marginLeft:"10%"}]}>
                        <Text style={[styles.buttonText, {color: "#0000008a"}]}> Lewati </Text>
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
                  </View>
                }
                dialogStyle={{borderRadius: 10}}
                width = {504}
                height= {232}
                ref={(popupDialog) => { this.popupDialog = popupDialog; }}>
              <View style={{padding: 30, paddingTop: 0, flex: 1, flexDirection: 'column', justifyContent: 'space-between', alignItems: 'center'}}>
                <Text style={{fontSize: 20, color: '#000000b3'}}>
                  Bukti Pembayaran
                </Text>
                <Text style={{fontSize: 14, color: '#000000b3'}}>
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
    headerTintColor: '#FFF',
    headerStyle: {
        backgroundColor: '#42B549'
    }
    // headerMode: 'none'
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
        fontSize: 16,
        color: '#000000b3',
        marginTop: 10
      },
      text2: {
        fontSize: 16,
        color:"#00000061"
      },
      text3: {
        marginTop: 10,
        fontSize: 14,
        color:"#000000b3"
      },
      row3text1: {
        fontSize: 12,
        color: '#000000b3',
        marginTop: 10
      },
      row3text2: {
        fontSize: 12,
        color:"#00000061"
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
      errorText : {
        color: '#D50000'
      },
      productImage: {
        borderRadius: 3,
        width: 62,
        height: 62,
        resizeMode: 'cover'
      },
        font13 : {
        fontSize: 13
      },
      fontcolor61 : {
        color: "#00000061"
      },
      font15 : {
        fontSize: 15
      },
      font14 : {
        fontSize: 14
      },
      font16 : {
        fontSize: 16
      },
      fontcolor54 : {
        color: "#0000008a"
      },
      fontcolor70 : {
        color: "#000000b3"
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