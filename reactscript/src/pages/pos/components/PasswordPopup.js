import React, { Component } from 'react'
import { 
  Text,
  View,
  StyleSheet,
  Image,
  TouchableNativeFeedback,
  TouchableWithoutFeedback,
  TextInput
} from 'react-native'
import PopupDialog, {DialogTitle} from 'react-native-popup-dialog';

class PasswordPopup extends Component {
  constructor(props) {
    super(props)
    this.state = { errorMessage: "", password: "" }
  }

  show(onShown: ?Function) {
    this.setState({password: "", errorMessage: ""})
    this.popupDialog.show(onShown);
  }

  dismiss(onDismissed: ?Function) {
    this.popupDialog.dismiss(onDismissed);
  }

  render() {

    return (
     <PopupDialog
        dialogTitle={
          <View style={{flexDirection:'row', justifyContent: 'flex-end', padding: 10}}> 
          </View>
        }
        dialogStyle={{borderRadius: 10}}
        width = {550}
        height= {250}
        ref={(popupDialog) => { this.popupDialog = popupDialog; }}>
      <View style={{padding: 30, paddingTop: 0, flex: 1,flexDirection: 'column', justifyContent: 'space-between', alignItems: 'center'}}>
        <Text style={{fontSize: 20, color:"#000000b3"}}>
          Riwayat Transaksi
        </Text>
        <View style={{padding: 20, width: '100%', marginTop: 30, paddingBottom: 0}}>
            <Text style={{fontSize: 12, color:"#0000008a"}}> Masukkan Password </Text>
            <TextInput
              style={{fontSize:16, width:"100%", marginTop:-10}}
              secureTextEntry={true}
              value={this.state.password}
              underlineColorAndroid="#e0e0e0"
              placeholderTextColor = "#e0e0e0"
              onChangeText={(password) => this.setState({password})}
            />
             <Text style={{fontSize: 12, color: '#D50000'}}> {this.state.errorMessage} </Text>
        </View>
        <View style={{padding: 10, flex: 1, flexDirection: 'row', justifyContent: 'space-between'}}>
          <TouchableNativeFeedback onPress={() => { this.popupDialog.dismiss()}}>
              <View style={[styles.popupButton, {backgroundColor: "#FFFFFF", borderColor:"#e0e0e0"}]}>
                <Text style={[styles.buttonText, {color: "#0000008a"}]}> Batal </Text>
            </View>
          </TouchableNativeFeedback>
            <TouchableNativeFeedback onPress={() => {
                    let errorMessage = "";
                    if (this.state.password != "") {
                      this.props.navigation.navigate('TransactionHistory', {})
                    } else {
                      errorMessage = "Kata sandi salah";
                      this.setState({errorMessage});
                    }
                } }>
            <View style={[styles.popupButton, {marginLeft: 30}]}>
              <Text style={styles.buttonText}> Lanjut </Text>
            </View>
          </TouchableNativeFeedback>
        </View>
      </View>
    </PopupDialog>
    )
  }
}

const styles = StyleSheet.create({
  popupButton: {
        height: 40,
        width:207,
        backgroundColor: '#42b549',
        alignItems: 'center',
        justifyContent: 'center',
        borderRadius: 3,
        borderWidth: 3,
        borderColor: '#41B548',
      },
  buttonText: {
    color: '#FFFFFF',
    fontSize: 13,
  }

})

export default PasswordPopup