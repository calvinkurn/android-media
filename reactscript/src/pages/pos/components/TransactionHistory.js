import React, { Component } from 'react';
import { connect } from 'react-redux';
import { StyleSheet, Text, View, Image, Button, TouchableWithoutFeedback, ScrollView, TextInput, TouchableNativeFeedback, ListView, FlatList} from 'react-native';
import { emailValidation } from '../lib/utility'
import PopupDialog, {DialogTitle} from 'react-native-popup-dialog';
import {getTransactionHistory } from '../actions/index';


import PopUp from '../common/TKPPopupModal'

class TransactionHistory  extends Component {

  constructor (props) {

    super (props);

    this.state = {
      selectedOrder : ""
    };
  }

  componentDidMount() {
    this.props.dispatch(getTransactionHistory());
  }

_toggleProduct(selectedOrder) {
    this.setState({
      selectedOrder
    });
  }

  _renderProductList(rowData) {

    return (  
      <View style={{flex: 1, flexDirection: 'row',justifyContent: 'space-between', marginTop: '1%'}}>
          <View style={{width: "20%", height: '20%'}}>
              <Image source={ {uri: rowData.imageUrl} } style={styles.productImage} ></Image>
          </View>
          <View style={{width: '60%', height: '20%', flexDirection: 'column', justifyContent: 'flex-start'}}>
              <Text style={{fontSize: 20, fontWeight: 'bold', width: '80%'}}>{rowData.name} </Text> 
              <Text style={{fontSize: 20, color: '#C8C8C8', marginTop: 10}}>Jumlah Barang: {rowData.qty}</Text> 
          </View>
          <Text style={{fontSize: 20, fontWeight: 'bold'}}>{rowData.price}</Text> 
      </View>
    );
  }
  _renderTarnsactionHistory(rowData: string, sectionID: number, rowID: number) {
    let ds = new ListView.DataSource({
      rowHasChanged: (r1, r2) => r1 !== r2 });
    let itemListddd = ds.cloneWithRows(rowData.products);

    return (
      <View style={styles.row}>  
        <View style={styles.subRow1}>
          <Text style={styles.orderName}>{rowData.orderName}</Text>
          <Text style={styles.orderId}>{rowData.orderId}</Text>
        </View>

        <View style={styles.subRow2}>
          <View style={{width: "22%"}}>
            <Text style={[styles.font20, styles.grey]}>Waktu Transaksi</Text>
            <Text style={styles.font20}>{rowData.time}</Text>
          </View>
          <View style={{width:"40%"}}>
            <Text style={[styles.font20, styles.grey]}>Barang</Text>
            <Text style={styles.font20}>{rowData.products[0].name.slice(0, 35)}...</Text>
          </View>
          <View style={{width:"25%"}}>
            <Text style={[styles.font20, styles.grey]}>Total Pembayaran</Text>
            <Text style={[styles.font20, styles.fontbold]}>{rowData.totalPrice}</Text>
          </View>
          
        </View>

        <View style={styles.subRow3}>
          <View>
          <Text style={[styles.font20, styles.grey]}>Status</Text>
          <Text style={styles.font20}>Status {rowData.status}</Text>
          </View>
          <View style={{marginTop: 25, flexDirection: 'row' }}>
            <TouchableNativeFeedback
              onPress={() =>
                    this._toggleProduct(this.state.selectedOrder != rowData.orderId ? rowData.orderId : "")
                  }
              >
               <Text style={[styles.font20,  {color: '#42b549'}]}> { (this.state.selectedOrder != rowData.orderId) ? "Lihat Details" : "Sembunyikan" }</Text>
            </TouchableNativeFeedback>
            <TouchableNativeFeedback
              onPress={() =>
                    this._toggleProduct(this.state.selectedOrder != rowData.orderId ? rowData.orderId : "")
                  }
              >
              { (this.state.selectedOrder != rowData.orderId) ? 
               <Image source={require('./img/arrow-down.png')} style={{height:12, width: 12, resizeMode: 'cover', marginTop: 8, marginLeft:7}} />
               :
                <Image source={require('./img/arrow-up.png')} style={{height:12, width: 12, resizeMode: 'cover', marginTop: 8, marginLeft:7}} />
             }
            </TouchableNativeFeedback>
          </View>
         
        </View>
        { (this.state.selectedOrder == rowData.orderId) ?
          
          <ListView
            contentContainerStyle={[styles.subRow4]}
            dataSource = { itemListddd}
            enableEmptySections={true}
            renderRow = {this._renderProductList.bind(this)} />
        : null
        }

        { (this.state.selectedOrder == rowData.orderId) ?      
            <View style={styles.subRow5} >
              <View style={{flexDirection: 'column'}}>
                  <Text style={{fontSize: 20}}>Metode Pembayaran</Text>
                  <View style={{flexDirection: 'row', alignItems: 'center' }}>
                    <Image source={require('./img/Logo-BCA.png')} style={{height: 50, width: 110, resizeMode: 'cover'}} >
                    </Image>
                    <Text style={{fontSize: 20, marginLeft: 10}}>BCA</Text>
                  </View>
              </View>         
                <View style={{flexDirection: 'row'}}>
                  <Text style={{fontSize: 20,fontWeight: 'bold'}}>Total Pembayaran</Text>
                  <Text style={{fontSize: 20, fontWeight: 'bold'}}> {rowData.totalPrice}</Text>
                </View>
            </View>
        : null}

         { (this.state.selectedOrder == rowData.orderId) ?      
            <View style={styles.subRow6} >
              <Text style={{fontSize:16}}> {(rowData.isCompleted) ? "Transaksi sudah melewati batas waktu untuk dibatalkan" : "Transaki dapat dibatalkan sebelum jam 19.00"}</Text>
             { (!rowData.isCompleted) ? 
              <TouchableNativeFeedback onPress={() => {this.popupDialog.show()}}>
                <View style={[styles.button]}>
                  <Text style={styles.buttonText}> Batalkan Transaksi </Text>
                </View>
              </TouchableNativeFeedback>
              :
                <TouchableNativeFeedback>
                <View style={[styles.button, {backgroundColor: '#E0E0E0', borderColor: '#E0E0E0'}]}>
                  <Text style={[styles.buttonText,  {color: '#000000'}]}> Batalkan Transaksi </Text>
                </View>
              </TouchableNativeFeedback>
            }
            </View>
        : null}
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
            <Text style={{ fontSize: 20, color: '#fff' }}>Riwayat Transaksi</Text>
          </View>
        </View>

         <ScrollView>
            <ListView
              contentContainerStyle={[styles.containers]}
              dataSource = { this.props.itemList}
              enableEmptySections={true}
              renderRow = {this._renderTarnsactionHistory.bind(this)} />
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
                <Text style={{fontSize: 20, fontWeight: 'bold'}}>
                  Bukti Pembayaran
                </Text>
                <View style={{alignItems:  'center'}}>
                <Text style={{fontSize: 20}}>
                  Anda yakin akan membatalkan transaksi
                </Text>
                <Text style={{fontSize: 20}}>
                  IVR/20170609/XVII/VI/13461163
                </Text>
                </View>
                <View style={{flexDirection: 'row'}}>
                <TouchableNativeFeedback onPress={() => { this.popupDialog.dismiss()}}>
                    <View style={[styles.popupButton, {backgroundColor: "#FFFFFF", borderColor:"#F3F3F3"}]}>
                      <Text style={[styles.buttonText, {color: "#888888"}]}> Tidak </Text>
                  </View>
                </TouchableNativeFeedback>
                  <TouchableNativeFeedback onPress={() => {
                          this.popupDialog.dismiss()
                      } }>
                  <View style={[styles.popupButton, {marginLeft: 30}]}>
                    <Text style={styles.buttonText}> Ya </Text>
                  </View>
                </TouchableNativeFeedback>
                </View>
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
        marginTop: 0
      },
      row: {
        backgroundColor: '#FFFFFF',
        borderRadius: 6,
        flex: 1,
        flexDirection: 'column',
        marginTop: 30
      },
      subRow1: {
        borderBottomWidth: 1,
        borderColor: '#F0F0F0',
        flexDirection: 'row',
        padding: "3%",
        justifyContent: 'space-between'
      },
      subRow2: {
        flexDirection: 'row',
        padding: "3%",
        justifyContent: 'space-between'
      },
      subRow3: {
        borderBottomWidth: 1,
        borderColor: '#F0F0F0',
        flexDirection: 'row',
        padding: "3%",
        justifyContent: 'space-between'
      },
      subRow4 : {
        borderBottomWidth: 1,
        borderColor: '#F0F0F0',
        flexDirection: 'column',
        justifyContent: 'flex-start',
        alignItems: 'flex-start',
        padding: "3%",
        paddingTop: 0
      },
      subRow5:{
        flexDirection: 'row',
        justifyContent: 'space-between',
        padding: "3%",
      },
      subRow6: {
        borderBottomWidth: 1,
        borderColor: '#F0F0F0',
        flexDirection: 'row',
        padding: "3%",
        justifyContent: 'space-between'
      },
      productImage: {
        borderRadius: 3,
        width: 100,
        height: 100,
        resizeMode: 'cover'
      },
      button: {
        height: 50,
        width: "30%",
        backgroundColor: '#41B548',
        alignItems: 'center',
        justifyContent: 'center',
        borderRadius: 3,
        borderWidth: 3,
        borderColor: '#41B548',
      },
       buttonText: {
        color: '#FFFFFF',
        fontSize: 18,
      },
      popupButton: {
        height: 50,
        width: "40%",
        backgroundColor: '#41B548',
        alignItems: 'center',
        justifyContent: 'center',
        borderRadius: 3,
        borderWidth: 3,
        borderColor: '#41B548',
      },
      orderName: {
        fontSize: 20,
        fontWeight: 'bold',
      },
      orderId: {
        fontSize: 20,
        color: '#C8C8C8'
      },
      font20 : {
        fontSize: 20
      },
      font22 : {
        fontSize: 22
      },
      fontbold : {
        fontWeight: 'bold'
      },
      grey: {
         color: '#C8C8C8',
      }
  });

const ds = new ListView.DataSource({
      rowHasChanged: (r1, r2) => r1 !== r2 });

const mapStateToProps = state => {
  const itemList = ds.cloneWithRows(state.transactionHistory.items);
  return {
    ...state.transactionHistory,
    itemList
  }
}

export default connect(mapStateToProps)(TransactionHistory)